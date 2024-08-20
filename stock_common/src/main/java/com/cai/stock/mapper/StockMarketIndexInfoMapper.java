package com.cai.stock.mapper;

import com.cai.stock.pojo.domain.ExternalMarKetDomain;
import com.cai.stock.pojo.domain.InnerMarketDomain;
import com.cai.stock.pojo.entity.StockMarketIndexInfo;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author cai
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2024-08-16 15:51:51
* @Entity com.cai.stock.pojo.entity.StockMarketIndexInfo
*/
@ApiModel(description = "针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper")
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    /**
     *根据指定的时间点查询指定大盘编码对应的数据
     * @param curDate 指定时间点
     * @param marketCodes 大盘编码集合
     * @return
     */
    List<InnerMarketDomain> getMarketInfo(@Param("curDate") Date curDate, @Param("marketCodes") List<String> marketCodes);

    /**
     * 根据时间范围和指定的大盘id统计每分钟的交易量
     * @param openDate 交易开始时间
     * @param endTime 交易结束时间
     * @param marketCodes 大盘id集合
     */
    List<Map> getSumAmtInfo(@Param("openDate") Date openDate, @Param("endTime") Date endTime, @Param("marketCodes") List<String> marketCodes) ;

    /**
     *指数行情数据查询，根据时间和大盘点数降序排序
     * @param curDate 当前时间
     * @param mCodes 股票编号
     * @return
     */
    List<ExternalMarKetDomain> getMarketInfoByTimeAndPoint(@Param("curDate") Date curDate, @Param("marketCodes") List<String> mCodes);
}
