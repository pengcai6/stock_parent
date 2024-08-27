package com.cai.stock.mapper;

import com.cai.stock.pojo.entity.StockBusiness;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author cai
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-08-16 15:51:51
* @Entity com.cai.stock.pojo.entity.StockBusiness
*/
@ApiModel(description = "针对表【stock_business(主营业务表)】的数据库操作Mapper")
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

    /**
     * 根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     * @param stockCode 个股代码
     * @return
     */
    List<Map<String, String>> getStockBuinessByCode(@Param("stockCode") String stockCode);

    /**
     * 根据输入的个股代码，查询个股信息
     * @param stockCode  个股代码
     * @return
     */
    Map<String, String> getStockDescribeByCode(@Param("stockCode") String stockCode);
}
