package com.cai.stock.service;
import com.cai.stock.pojo.domain.*;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;
import com.cai.stock.pojo.entity.StockRt;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 股票服务接口
 */
public interface StockService {
    /**
     * 获取国内大盘最新的数据
     * @return
     */
    R<List<InnerMarketDomain>> getInnerMarketInfo();
    /**
     * 查询沪深两市最新的板块行情数据，并按照交易金额降序排序展示前10条记录
     * @return
     */
       R<List<StockBlockDomain>> sectorAllLimit();
    /**
     * 分页查询最新的股票交易数据
     * @param page 当前页
     * @param pageSize 每页大小
     * @return
     */
    R<PageResult<StockUpdownDomain>> getStockInfoByPage(Integer page, Integer pageSize);

    /**
     * 统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据
     * @return
     */
    R<List<StockUpdownDomain>> getStockInfo4ByPage();
    /**
     * 统计沪深两市T日(当前股票交易日)每分钟达到涨跌停股票的数据
     * @return
     */
    R<Map<String, List>> getStockUpDownCount();
    /**
     * 导出指定页码的最新股票信息
     * @param page 页码
     * @param pageSize 每页大小
     * @param response 响应
     */
    void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response);

    /**
     * 统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * @return
     */
    R<Map<String, List>> getComparedStockTradeAmt();
    /**
     * 统计当前时间下（精确到分钟），A股在各个涨跌区间股票的数量
     * @return
     */
    R<Map> getIncreaseStockInfo();
    /**
     * 获取指定股票指定T日每分钟交易数据
     * @param stockCode 股票编码
     * @return
     */
    R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode);
    /**
     * 获取国外大盘最新的数据
     * @return
     */
    R<List<ExternalMarKetDomain>> getExternalMarketInfo();
    /**
     *  单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     * @return
     */
    R<List<Stock4EvrDayDomain>> getStockScreenDkLine(String stockCode);

    /**
     *  根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     * @param stockCode  个股代码
     * @return
     */
    R<List<Map<String, String>>> getStockSearch(String stockCode);
    /**
     * 个股主营业务查询
     * @param stockCode 个股代码
     * @return
     */
    R<Map<String, String>> getStockDescribe(String stockCode);
    /**
     *  单个个股周K 数据查询 ，可以根据时间区间查询数周的K线数据
     * @param stockCode 股票编码
     * @return
     */
    R<List<Stock4EvrWeekDomain>> getStockScreenWkLine(String stockCode);
    /**
     * 获取个股最新分时行情数据，主要包含：
     * 开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息;
     *
     * @param stockCode 股票编码
     * @return
     */
    R<StockRt> getStockRtInfoNow(String stockCode);
    /**
     * 个股交易流水行情数据查询--查询最新交易流水，按照交易时间降序取前10
     * @param stockCode 股票编码
     * @return
     */
    R<List<Map<String, Object>>> getStockStatement(String stockCode);
}
