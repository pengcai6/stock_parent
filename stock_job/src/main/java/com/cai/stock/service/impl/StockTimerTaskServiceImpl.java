package com.cai.stock.service.impl;

import com.cai.stock.constant.ParseType;
import com.cai.stock.mapper.StockBlockRtInfoMapper;
import com.cai.stock.mapper.StockBusinessMapper;
import com.cai.stock.mapper.StockMarketIndexInfoMapper;
import com.cai.stock.mapper.StockRtInfoMapper;
import com.cai.stock.pojo.entity.StockBlockRtInfo;
import com.cai.stock.pojo.entity.StockMarketIndexInfo;
import com.cai.stock.pojo.entity.StockRtInfo;
import com.cai.stock.pojo.vo.StockInfoConfig;
import com.cai.stock.service.StockTimerTaskService;
import com.cai.stock.utils.DateTimeUtil;
import com.cai.stock.utils.IdWorker;
import com.cai.stock.utils.ParserStockInfoUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper  stockBusinessMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    /*
    必须保证该对象初始化无状态
     */
    private  HttpEntity<Object> entity;
    @Override
    public void getInnerMarketInfo() {
        //1.阶段1：采集数据
        //1.1组装URL地址
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());
//        //1.2.维护请求头，添加防盗链和用户标识
//        HttpHeaders  httpHeaders=new HttpHeaders();
//        //防盗链
//        httpHeaders.add("Referer","https://finance.sina.com.cn/");
//        //用户标识
//        httpHeaders.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
//        //维护http请求实体类
//        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
        //发起请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        //出现异常操作
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue !=200) {
        log.error("当前时间点：{}，采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
        //其它操作
        return;
        }
        //获取原始js数据
        String jsData = responseEntity.getBody();
        log.error("当前时间点：{}，采集数据内容：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), jsData);
        //2.阶段2：java正则解析原始数据
        //2.1定义正则表达式
        String reg="var hq_str_(.+)=\"(.+)\";";
        //2.2将表达式编译
        Pattern pattern = Pattern.compile(reg);
        //2.3匹配字符串
        Matcher matcher = pattern.matcher(jsData);
        List<StockMarketIndexInfo> list=new ArrayList<>();
        while (matcher.find()) {
            //获取大盘编码
            String marketCode = matcher.group(1);
            //获取其他信息
            String otherInfo = matcher.group(2);
            //将其他信息以逗号分隔，获取大盘的详细信息
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName=splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint=new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint=new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint=new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint=new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint=new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt=Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol=new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            //组装entity对象
            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();
            //收集封装的对象，方便批量插入
            list.add(info);
        }
        log.info("解析大盘数据完毕！");
        //4.阶段4：调用mybatis插入数据
//     int count=   stockMarketIndexInfoMapper.insertBatch(list);
        int count=2;
        if(count>0){
         //大盘数据采集完毕后，通知backend工程刷新缓存
         //发送日期对象，接收方通过接受的日期与当前日期比对，能判断出数据延迟的时长，用于运维通知处理
         rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());
         log.info("当前时间:{},插入大盘数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
     }else {
         log.info("当前时间:{},插入大盘数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);

     }
    }

    @Override
    public void getStockRtInfo() {
        //1.获取所有个股的集合 3000
        List<String> allcodes = stockBusinessMapper.getAllStockCodes();

        //添加业务前缀sh,zh
        allcodes = allcodes.stream().map(code->code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        //一次性将所有的集合拼接到url中，导致url过长，参数过多
        // String url= stockInfoConfig.getMarketUrl()+String.join(",",allcodes);
//        System.out.println("allcodes = " + allcodes);
        //将大的集合拆分为若干小的集合
        //核心思路：分批次拉取数据
        Lists.partition(allcodes, 15).forEach(code->{
            String url= stockInfoConfig.getMarketUrl()+String.join(",",code);
//            //1.2.维护请求头，添加防盗链和用户标识
//            HttpHeaders  httpHeaders=new HttpHeaders();
//            //防盗链
//            httpHeaders.add("Referer","https://finance.sina.com.cn/");
//            //用户标识
//            httpHeaders.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
//            //维护http请求实体类
//            HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);
            //发起请求
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            //出现异常操作
            int statusCodeValue = responseEntity.getStatusCodeValue();
            if (statusCodeValue !=200) {
                log.error("当前时间点：{}，采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
                //其它操作
                return;
            }
            //获取原始js数据
            String jsData = responseEntity.getBody();
            //调用工具类解析数据
            List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
            log.info("采集个股数据：{}",list);
            //TODO:批量插入
          int count=  stockRtInfoMapper.insertBatch(list);
            if(count>0){
                log.info("当前时间:{},插入个股数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }else {
                log.info("当前时间:{},插入个股数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);

            }
        });

    }

    /**
     * 板块实时数据采集
     */
    @Override
    public void getStockSectorRtIndex() {
        //1.获取所有板块的集合
        String url=stockInfoConfig.getBlockUrl();
        //2.发起请求
        String result = restTemplate.getForObject(url, String.class);
        //3.响应结果转板块集合数据
        List<StockBlockRtInfo> infos = parserStockInfoUtil.parse4StockBlock(result);
        log.info("板块数据量:{}",infos.size());
        //4.TODO:批量插入
        Lists.partition(infos,20).forEach(
                info->{
                    int count=  stockBlockRtInfoMapper.insertBatch(info);
                    if(count>0){
                        log.info("当前时间:{},插入板块数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),info);
                    }else {
                        log.info("当前时间:{},插入板块数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),info);
                    }
                }
        );
    }

    @PostConstruct
    public void initData(){
        //1.2.维护请求头，添加防盗链和用户标识
        HttpHeaders  httpHeaders=new HttpHeaders();
        //防盗链
        httpHeaders.add("Referer","https://finance.sina.com.cn/");
        //用户标识
        httpHeaders.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0");
        //维护http请求实体类
       entity = new HttpEntity<>(httpHeaders);
    }

}
