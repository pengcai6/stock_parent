package com.cai.stock.mapper;

import com.cai.stock.pojo.domain.Stock4EvrDayDomain;
import com.cai.stock.pojo.domain.Stock4EvrWeekDomain;
import com.cai.stock.pojo.domain.Stock4MinuteDomain;
import com.cai.stock.pojo.domain.StockUpdownDomain;
import com.cai.stock.pojo.entity.StockRt;
import com.cai.stock.pojo.entity.StockRtInfo;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author cai
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-08-16 15:51:51
* @Entity com.cai.stock.pojo.entity.StockRtInfo
*/
@ApiModel(description = "针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper")
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

    /**
     * 查询指定时间点下股票数据的集合
     * @param curDate 日期时间
     * @return
     */
    List<StockUpdownDomain> getStockInfoByTime(@Param("curDate") Date curDate);

    /**
     * 统计指定时间内的股票涨停或者跌停的数量流水
      * @param startDate 开始时间，一般指开盘时间
     * @param endDate 截止时间
     * @param flag 约点，0表示跌停，1表示涨停
     * @return
     */
    List<Map> getStockUpdownCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("flag") int flag);

    /**
     * 统计当前时间下（精确到分钟），A股在各个涨跌区间股票的数量
     * @param curDate 当前时间
     * @return
     */
    List<Map> getIncreaseStockInfoByDate(@Param("dateTime") Date curDate);

    /**
     * 获取指定股票指定T日每分钟交易数据
     * @param startDate 开盘时间
     * @param endDate 截止时间2
     * @param stockCode 股票编号
     * @return
     */
    List<Stock4MinuteDomain> getStockInfo4Minute(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);

    /**
     *  查询指定日期范围内指定股票每天的交易数据
     * @param startDate 开始日期
     * @param endDate 截止日期
     * @param stockCode 股票编码
     * @return
     */
    List<Stock4EvrDayDomain> getStock4DkLine(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);

    /**
     * 查询指定股票在指定日期范围内的每天的最大时间；
     *
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @param stockCode 股票编号
     * @return
     */
    List<Date> getStockMaxTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);

    /**
     * 查询指定日期范围内指定股票每天的交易数据通过已有时间数据
     * @param StockMaxTimes 最大时间数组
     * @param stockCode 股票编号
     * @return
     */
    List<Stock4EvrDayDomain> getStock4DkLineByMaxTime(@Param("StockMaxTimes") List<Date> StockMaxTimes, @Param("stockCode") String stockCode);

    /**
     * 批量插入个股数据
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<StockRtInfo> list);

    /**
     * 查询指定股票在指定日期范围内的每周的最大时间；
     *
     * @param startDate
     * @param endDate
     * @param stockCode
     * @return
     */
    List<Date> getStockMaxWeekTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);

    /**
     * 个个股周K 数据查询 ，可以根据时间区间查询数周的K线数据
     * @param weekStartDate 周开始时间
     * @param weekEndDate 周结束时间
     * @param stockCode 股票编号
     * @return
     */
    Stock4EvrWeekDomain getStock4WkLineByMaxTime(@Param("weekStartDate") Date weekStartDate, @Param("weekEndDate") Date weekEndDate, @Param("stockCode") String stockCode);

    /**
     * 获取给定时间的开盘价
     *
     * @param Date 时间
     * @param stockCode 股票代码
     * @return
     */
    BigDecimal getStockOpenPrice(@Param("Date") Date Date, @Param("stockCode") String stockCode);

    /**
     * 获取给定时间的收盘价
     * @param Date 时间
     * @param stockCode 股票代码
     * @return
     */
    BigDecimal getStockClosePrice(@Param("Date") Date Date, @Param("stockCode") String stockCode);

    /**
     * 获取个股最新分时行情数据
     * @param stockCode  股票代码
     * @param curDate  时间
     * @return
     */
    StockRt getStockRtInfo(@Param("stockCode") String stockCode, @Param("curDate") Date curDate);

    /**
     * 个股交易流水行情数据查询
     * @param stockCode 股票代码
     * @return
     */
    List<Map<String, Object>> getStockStatement(@Param("stockCode") String stockCode);
}
