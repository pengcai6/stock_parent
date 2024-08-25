package com.cai.stock;

import com.cai.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRestTemplate {

    @Autowired StockTimerTaskService  stockTimerTaskService;
    @Test
    public void test01(){
        stockTimerTaskService.getInnerMarketInfo();
//        stockTimerTaskService.getStockRtInfo();
    }
    @Test
    public void test03() throws InterruptedException {
        stockTimerTaskService.getStockSectorRtIndex();
    }
}
