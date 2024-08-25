package com.cai.stock;

import com.cai.stock.mapper.StockBusinessMapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TestMapper {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    /**
     * 测速获取所有个股编码集合
     */
    @Test
    public void test(){
        List<String> codes = stockBusinessMapper.getAllStockCodes();
 //       System.out.println("codes = " + codes);
        //添加业务前缀sh,zh
       codes= codes.stream().map(code->code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        System.out.println("codes = " + codes);
        //将大的集合拆分为若干小的集合
    Lists.partition(codes, 15).forEach(code->{
        System.out.println("code = " + code);
    });

    }
}
