package com.cai.stock.face.impl;

import com.cai.stock.face.StockCacheFace;
import com.cai.stock.mapper.StockBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定义股票缓存层的实现
 */
@Component("stockCacheFace")
public class StockCacheFaceImpl implements StockCacheFace {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Cacheable(cacheNames = "stock",key = "'stockeCodes'")
    @Override
    public List<String> getAllStockCodeWithPredix() {
        //1.获取所有个股的集合 3000
        List<String> allcodes = stockBusinessMapper.getAllStockCodes();

        //添加业务前缀sh,zh
        allcodes = allcodes.stream().map(code -> code.startsWith("6") ? "sh" + code : "sz" + code).collect(Collectors.toList());

        return allcodes;
    }
}
