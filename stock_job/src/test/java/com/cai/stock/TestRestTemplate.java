package com.cai.stock;

import com.cai.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRestTemplate {

    @Autowired StockTimerTaskService  stockTimerTaskService;
    @Test
    public void test01() throws InterruptedException {
//        stockTimerTaskService.getInnerMarketInfo();
        stockTimerTaskService.getStockRtInfo();
        //目的，让主线程休眠
        Thread.sleep(5000);
    }
    @Test
    public void test03() throws InterruptedException {
        stockTimerTaskService.getStockSectorRtIndex();
    }
}
