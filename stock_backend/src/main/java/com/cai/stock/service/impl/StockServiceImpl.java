package com.cai.stock.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.cai.stock.mapper.StockBlockRtInfoMapper;
import com.cai.stock.mapper.StockMarketIndexInfoMapper;
import com.cai.stock.mapper.StockRtInfoMapper;
import com.cai.stock.pojo.domain.InnerMarketDomain;
import com.cai.stock.pojo.domain.StockBlockDomain;
import com.cai.stock.pojo.domain.StockUpdownDomain;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                log.error("exportStockUpDownInfo响应错误信息失败，时间：{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
        }

    }
}
