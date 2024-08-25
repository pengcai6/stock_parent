package com.cai.stock.mapper;

import com.cai.stock.pojo.entity.StockBusiness;

import java.util.List;

/**
* @author cai
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-08-16 15:51:51
* @Entity com.cai.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    /**
     * 获取A股个股的编码集合
     * @return
     */
    List<String> getAllStockCodes();
}
