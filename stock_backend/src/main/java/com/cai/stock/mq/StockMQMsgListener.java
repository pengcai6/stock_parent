package com.cai.stock.mq;

import com.cai.stock.service.StockService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定义股票相关mq消息监听
 */
@Component
@Log4j2
public class StockMQMsgListener {
    @Autowired
    private Cache<String,Object> caffeineCache;
    @Autowired
    private StockService stockService;

    @RabbitListener(queues = "innerMarketQueue")
    public void refreshInnerMarketInfo(Date startTime){
        //统计当前时间点与发送时间点的差值，如果超过一分钟，则告警
        //获取时间毫秒差值
        long diffTime= DateTime.now().getMillis()-new DateTime(startTime).getMillis();
        if (diffTime>60000l) {
        log.error("大盘接收后时间：{},延迟：{}ms",new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"),diffTime);
        }
        //刷新缓存
        //剔除旧的数据
        caffeineCache.invalidate("innerMarketKey");
        //调用服务方法，刷新缓存
        stockService.getInnerMarketInfo();
    }

}
