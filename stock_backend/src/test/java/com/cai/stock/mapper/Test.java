package com.cai.stock.mapper;

import com.cai.stock.pojo.domain.Stock4EvrDayDomain;
import com.cai.stock.utils.DateTimeUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class Test {
    @Autowired
    StockRtInfoMapper stockRtInfoMapper;

    /**
     * 查询指定股票在指定日期范围内的每天的最大时间；
     */
   @org.junit.jupiter.api.Test
    public void test(){
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        endDateTime = DateTime.parse("2021-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        //1.2起始时间
        Date startDate = endDateTime.minusMonths(3).toDate();
        startDate = DateTime.parse("2021-6-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<String> stockMaxTime = stockRtInfoMapper.getStockMaxTime(startDate,endDate,"600016");

       List<Stock4EvrDayDomain> stock4DkLineByMaxTime = stockRtInfoMapper.getStock4DkLineByMaxTime(stockMaxTime, "600016");

       System.out.println("stock4DkLineByMaxTime = " + stock4DkLineByMaxTime);
   }




}
