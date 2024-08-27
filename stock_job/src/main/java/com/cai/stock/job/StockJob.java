package com.cai.stock.job;

import com.cai.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.checkerframework.checker.units.qual.A;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义xxlJob任务执行器bean
 */
@Component
public class StockJob {
    @Autowired
  private   StockTimerTaskService stockTimerTaskService;

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("myJobHandler")
    public void demoJobHandler() throws Exception {
        System.out.println("DateTime" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
    }
    /**
     * 定时采集A股大盘数据
     */
    @XxlJob("getInnerMarketInfo")
    public void getStockInfos(){
        stockTimerTaskService.getInnerMarketInfo();
    }
    /**
     * 定时采集A股个股数据
     */
    @XxlJob("getStockRtInfo")
    public void getStockRtInfo(){
        stockTimerTaskService.getStockRtInfo();
    }

}
