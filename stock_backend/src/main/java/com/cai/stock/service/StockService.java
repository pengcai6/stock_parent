package com.cai.stock.service;
import cn.hutool.http.server.HttpServerResponse;
import com.cai.stock.pojo.domain.InnerMarketDomain;
import com.cai.stock.pojo.domain.StockBlockDomain;
import com.cai.stock.pojo.domain.StockUpdownDomain;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;

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
}
