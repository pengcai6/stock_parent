package com.cai.stock.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.cai.stock.mapper.StockBlockRtInfoMapper;
import com.cai.stock.mapper.StockMarketIndexInfoMapper;
import com.cai.stock.mapper.StockRtInfoMapper;
import com.cai.stock.pojo.domain.*;
import com.cai.stock.pojo.vo.StockInfoConfig;
import com.cai.stock.service.StockService;
import com.cai.stock.utils.DateTimeUtil;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@ApiModel
@Service
@Log4j2
public class StockServiceImpl implements StockService {
    @ApiModelProperty(hidden = true)
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @ApiModelProperty(hidden = true)
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @ApiModelProperty(hidden = true)
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockRtInfoMapper StockRtInfoMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        //1.获取股票交易的最新时间点（精确到分钟，秒和毫秒置为0）
//        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
//        Date curDate = curDateTime.toDate();
        //由于没有采集数据，目前使用假数据调试
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-07-07 14:51:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.获取大盘集合编码
        List<String> mCodes = stockInfoConfig.getInner();
        //3.调用mapper查询数据
        List<InnerMarketDomain> data = stockMarketIndexInfoMapper.getMarketInfo(curDate, mCodes);
        //4.封装并且返回数据
        return R.ok(data);
    }
    /**
     * 获取国外大盘最新的数据
     * @return
     */
    @Override
    public R<List<ExternalMarKetDomain>> getExternalMarketInfo() {
        //1.获取股票交易的最新时间点
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-05-18 15:58:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.获取大盘集合编码
        List<String> mCodes = stockInfoConfig.getOuter();
        //3.调用mapper查询数据,根据时间和大盘点数降序排序取前4
        Integer PageSize=4;
        PageHelper.startPage(1,PageSize);
        List<ExternalMarKetDomain> data = stockMarketIndexInfoMapper.getMarketInfoByTimeAndPoint(curDate, mCodes);
        //4.封装并且返回数据
        return R.ok(data);
    }
    /**
     *  单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     * @return
     */
    @Override
    public R<List<Stock4EvrDayDomain>> getStockScreenDkLine(String stockCode) {
        //1.获取统计日K线的数据的时间范围
        //1.1获取截止时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        endDateTime = DateTime.parse("2021-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        //1.2起始时间
        Date startDate = endDateTime.minusMonths(3).toDate();
        startDate = DateTime.parse("2021-6-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //2.调用Mapper接口获取指定日期范围内的日K线数据
        List<Stock4EvrDayDomain> infos  =stockRtInfoMapper.getStock4DkLine(startDate,endDate,stockCode);
        //3.返回数据
        return R.ok(infos);
    }

    /**
     * 需求说明: 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     *
     * @return
     */
    @Override
    public R<List<StockBlockDomain>> sectorAllLimit() {
        //由于没有采集数据，目前使用假数据调试
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2021-12-21 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //调用mapper查询数据
        List<StockBlockDomain> data = stockBlockRtInfoMapper.sectorAllLimit(curDate);
        //组装数据
        if (CollectionUtil.isEmpty(data)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(data);
    }

    /**
     * 分页查询最新的股票交易数据
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return
     */
    @Override
    public R<PageResult<StockUpdownDomain>> getStockInfoByPage(Integer page, Integer pageSize) {
        //1.获取股票交易的最新时间点
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.设置pageHelper分页参数
        PageHelper.startPage(page, pageSize);
        //3.调用mapper接口查询
        List<StockUpdownDomain> pageData = StockRtInfoMapper.getStockInfoByTime(curDate);
        //4.组装pageResult对象
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(pageData);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        //5.响应数据
        return R.ok(pageResult);
    }

    /**
     * 统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据
     *
     * @return
     */
    @Override
    public R<List<StockUpdownDomain>> getStockInfo4ByPage() {
        //1.获取股票交易的最新时间点
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.设置pageHelper分页参数
        PageHelper.startPage(1, 4);
        //3.调用mapper接口查询
        List<StockUpdownDomain> pageData = StockRtInfoMapper.getStockInfoByTime(curDate);
        //4.响应数据
        return R.ok(pageData);
    }

    /**
     * 统计沪深两市T日(当前股票交易日)每分钟达到涨跌停股票的数据
     *
     * @return
     */
    @Override
    public R<Map<String, List>> getStockUpDownCount() {
        //1.获取股票交易的最新时间点(截止时间)
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        curDateTime = DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = curDateTime.toDate();
        //2.获取最新交易时间对应的开盘时间点
        Date startDate = DateTimeUtil.getOpenDate(curDateTime).toDate();
        //3.统计涨停数据
        List<Map> upList = StockRtInfoMapper.getStockUpdownCount(startDate, endDate, 1);
        //4.统计跌停
        List<Map> downList = StockRtInfoMapper.getStockUpdownCount(startDate, endDate, 0);
        //5.组装数据
        HashMap<String, List> info = new HashMap<>();
        info.put("upList", upList);
        info.put("downList", downList);
        //6.响应数据
        return R.ok(info);
    }

    /**
     * 导出指定页码的最新股票信息
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param response 响应
     */
    @Override
    public void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response) {
        //1.获取分页数据
        R<PageResult<StockUpdownDomain>> r = this.getStockInfoByPage(page, pageSize);
        Integer pageNum = r.getData().getPageNum();
        List<StockUpdownDomain> rows = r.getData().getRows();
        //2.将数据导出到excel中
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("股票信息表", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票涨幅信息").doWrite(rows);
        } catch (IOException e) {
            log.error("当前页码：{}，每页大小：{}，当前时间：{}，异常信息：{}", page, pageSize, DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), e.getMessage());
            //通知前端异常，稍后再试
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            R<Object> error = R.error(ResponseCode.ERROR);
            try {
                String jsonData = new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(jsonData);
            } catch (IOException ex) {
                log.error("exportStockUpDownInfo响应错误信息失败，时间：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
        }

    }

    /**
     * 统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *
     * @return
     */
    @Override
    public R<Map<String, List>> getComparedStockTradeAmt() {
        //1.获取T日（最新股票交易日的日期范围）
        DateTime tEndDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date tEndTime = tEndDateTime.toDate();
        //开盘时间
        Date tStartDate = DateTimeUtil.getOpenDate(tEndDateTime).toDate();
        //TODO  mock数据
        tStartDate = DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        tEndTime = DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //获取T-1日的时间范围
        DateTime preTEndDateTime = DateTimeUtil.getPreviousTradingDay(tEndDateTime);
        Date preTEndDate = preTEndDateTime.toDate();
        //开盘时间
        Date tPreStartDate = DateTimeUtil.getOpenDate(preTEndDateTime).toDate();
        //TODO  mock数据
        tPreStartDate = DateTime.parse("2022-01-02 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        preTEndDate = DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //3.调用mapper查询
        //3.1统计T日
        List<Map> tData = stockMarketIndexInfoMapper.getSumAmtInfo(tStartDate, tEndTime, stockInfoConfig.getInner());
        //3.2统计T-1日
        List<Map> preTData = stockMarketIndexInfoMapper.getSumAmtInfo(tPreStartDate, preTEndDate, stockInfoConfig.getInner());
        //4.组装数据
        HashMap<String, List> info = new HashMap<>();
        info.put("amtList", tData);
        info.put("yesAmtList", preTData);
        //5.返回数据
        return R.ok(info);
    }

    /**
     * 统计当前时间下（精确到分钟），A股在各个涨跌区间股票的数量
     *
     * @return
     */
    @Override
    public R<Map> getIncreaseStockInfo() {
        //1.获取当前时间
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        curDateTime = DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = curDateTime.toDate();
        //2.调用mapper接口
        List<Map> infos = stockRtInfoMapper.getIncreaseStockInfoByDate(curDate);
        //获取有序涨幅区间标题集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        //将顺序的涨幅区间内的每个元素转为map对像
//        List<Map> allInfos = new ArrayList<>();
//        for (String title : upDownRange) {
//            Map tmp=null;
//            for (Map info : infos) {
//                if (info.containsValue(title)) {
//                   tmp=info;
//                   break;
//                }
//            }
//            if (tmp==null) {
//                //不存在，则补齐
//                tmp=new HashMap();
//                tmp.put("title", title);
//                tmp.put("count", 0);
//            }
//            allInfos.add(tmp);
//        }
//使用steam遍历获取
        List<Map> allInfos = upDownRange.stream().map(title -> {
            Map mp = null;
            Optional<Map> op = infos.stream().filter(m -> m.containsValue(title)).findFirst();
            //判断是否存在符合过滤条件的元素
            if (op.isPresent()) {
                mp = op.get();
            } else {
                mp = new HashMap();
                mp.put("count", 0);
                mp.put("title", title);
            }
            return mp;
        }).collect(Collectors.toList());
        //3.组装数据
        Map<String, Object> data = new HashMap<>();
        data.put("time", curDateTime.toString("yyyy-MM-dd HH:mm:ss"));
//        data.put("infos",infos);
        data.put("infos", allInfos);
        //4.返回数据
        return R.ok(data);
    }

    /**
     * 获取指定股票指定T日每分钟交易数据
     *
     * @param stockCode 股票编码
     * @return
     */
    @Override
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode) {
        //1.获取当前时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        endDateTime = DateTime.parse("2021-12-30 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        //2.获取开盘时间
        Date openDate = DateTimeUtil.getOpenDate(endDateTime).toDate();
        //3.调用mapper接口查询
       List<Stock4MinuteDomain> data = stockRtInfoMapper.getStockInfo4Minute(openDate,endDate,stockCode);
        //判断非空处理
        if (CollectionUtils.isEmpty(data)) {
            data=new ArrayList<>();
        }
       //4.返回数据
        return R.ok(data);
    }


}
