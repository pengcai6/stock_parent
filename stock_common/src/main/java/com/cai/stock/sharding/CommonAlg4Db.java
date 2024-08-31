package com.cai.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 定义公共的分库算法类，覆盖个股 大盘 板块
 */
public class CommonAlg4Db implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
    /**
     * 精准查询走该方法
     * @param dsNames
     * @param ShardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> dsNames, PreciseShardingValue<Date> ShardingValue) {
        //获取逻辑表
        String logicTableName = ShardingValue.getLogicTableName();
        //获取条件‘
        Date value = ShardingValue.getValue();
        //获取分片键
        String columnName = ShardingValue.getColumnName();
        //获取条件值对应的年份，然后从dS集合中过滤出以该年份结尾的数据源即可
        String year = new DateTime(value).getYear()+"";
        Optional<String> result = dsNames.stream().filter(dsName -> dsName.endsWith(year)).findFirst();
        if(result.isPresent()){
            return result.get();
        }
        return null;
    }

    /**
     * 范围查询 between
     * @param dsNames
     * @param ShardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> dsNames, RangeShardingValue<Date> ShardingValue) {
        //获取逻辑表
        String logicTableName = ShardingValue.getLogicTableName();
        //获取分片键
        String columnName = ShardingValue.getColumnName();
        //获取范围数据封装
        Range<Date> valueRange = ShardingValue.getValueRange();
        //判断下限
        if (valueRange.hasLowerBound()) {
            Date startTime = valueRange.lowerEndpoint();
            //获取条件所属年份
            int startYear = new DateTime(startTime).getYear();
            //过滤出年份大于等于startYear数据即可
            dsNames = dsNames.stream().filter(dsName -> Integer.parseInt(dsName.substring(dsName.lastIndexOf("-") + 1)) >= startYear).collect(Collectors.toList());

        }
        //判断上限
        if (valueRange.hasUpperBound()) {
            Date endTime = valueRange.upperEndpoint();
            //获取条件所属年份
            int endYear = new DateTime(endTime).getYear();
            //过滤出年份大于等于startYear数据即可
            dsNames = dsNames.stream().filter(dsName -> Integer.parseInt(dsName.substring(dsName.lastIndexOf("-") + 1)) <= endYear).collect(Collectors.toList());
        }


        return dsNames;
    }
}
