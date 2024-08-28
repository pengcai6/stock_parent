package com.cai.stock.service;

/**
 *
 * @Description 定义采集股票数据的定时任务的服务接口
 */
public interface StockTimerTaskService {
    /**
     * 获取国内大盘的实时数据信息
     */
    void getInnerMarketInfo();

    /**
     * 定义获取分钟级股票数据
     */
    void getStockRtInfo();

    /**
     *  板块实时数据采集
     */
   void  getStockSectorRtIndex();
    /**
     * 定时采集国外大盘数据
     */
    void getOuterRtInfo();
}