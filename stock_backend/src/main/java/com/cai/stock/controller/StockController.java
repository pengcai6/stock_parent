package com.cai.stock.controller;

import cn.hutool.http.server.HttpServerResponse;
import com.cai.stock.mapper.StockRtInfoMapper;
import com.cai.stock.pojo.domain.*;
import com.cai.stock.service.StockService;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 定义股票接口相关控制器
 */
@Api(value = "/api/quot", tags = {"定义股票接口相关控制器"})
@RestController()
@RequestMapping("/api/quot")
public class StockController {
    @Autowired
    private StockService stockService;

    /**
     * 获取国内大盘最新的数据
     *
     * @return
     */
    @ApiOperation(value = "获取国内大盘最新的数据", notes = "获取国内大盘最新的数据", httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        return stockService.getInnerMarketInfo();
    }

    /**
     * 获取国外大盘最新的数据
     * @return
     */
    @ApiOperation(value = "获取国外大盘最新的数据", notes = "获取国外大盘最新的数据", httpMethod = "GET")
    @GetMapping("/external/index")
    public R<List<ExternalMarKetDomain>> getExternalMarketInfo(){
        return stockService.getExternalMarketInfo();
    }
    /**
     * 查询沪深两市最新的板块行情数据，并按照交易金额降序排序展示前10条记录
     *
     * @return
     */
    @ApiOperation(value = "查询沪深两市最新的板块行情数据，并按照交易金额降序排序展示前10条记录", notes = "查询沪深两市最新的板块行情数据，并按照交易金额降序排序展示前10条记录", httpMethod = "GET")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAll() {
        return stockService.sectorAllLimit();
    }

    /**
     * 分页查询最新的股票交易数据
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页大小")
    })
    @ApiOperation(value = "分页查询最新的股票交易数据", notes = "分页查询最新的股票交易数据", httpMethod = "GET")
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockInfoByPage(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return stockService.getStockInfoByPage(page, pageSize);
    }

    /**
     * 统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据
     * @return
     */
    @ApiOperation(value = "统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据", notes = "统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据", httpMethod = "GET")
    @GetMapping("/stock/increase")
    public  R<List<StockUpdownDomain>> getStockInfo4ByPage() {
        return stockService.getStockInfo4ByPage();
    }

    /**
     * 统计沪深两市T日(当前股票交易日)每分钟达到涨跌停股票的数据
     * @return
     */
    @ApiOperation(value = "统计沪深两市T日(当前股票交易日)每分钟达到涨跌停股票的数据", notes = "统计沪深两市T日(当前股票交易日)每分钟达到涨跌停股票的数据", httpMethod = "GET")
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpDownCount(){
        return stockService.getStockUpDownCount();
    }

    /**
     * 导出指定页码的最新股票信息
     * @param page 页码
     * @param pageSize 每页大小
     * @param response 响应
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页大小")
    })
    @ApiOperation(value = "导出指定页码的最新股票信息", notes = "导出指定页码的最新股票信息", httpMethod = "GET")
    @GetMapping("/stock/export")
    public void exportStockUpDownInfo(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize, HttpServletResponse response) {
        stockService.exportStockUpDownInfo(page, pageSize, response);
    }

    /**
     * 统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * @return
     */
    @ApiOperation(value = "统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）", notes = "统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）", httpMethod = "GET")
    @GetMapping("/stock/tradeAmt")
    public R<Map<String,List>> getComparedStockTradeAmt(){
        return stockService.getComparedStockTradeAmt();
    }

    /**
     * 统计当前时间下（精确到分钟），A股在各个涨跌区间股票的数量
     * @return
     */
    @ApiOperation(value = "统计当前时间下（精确到分钟），A股在各个涨跌区间股票的数量", notes = "统计当前时间下（精确到分钟），A股在各个涨跌区间股票的数量", httpMethod = "GET")
    @GetMapping("/stock/updown")
    public R<Map> getIncreaseStockInfo(){
    return stockService.getIncreaseStockInfo();
    }

    /**
     * 获取指定股票指定T日每分钟交易数据
     * @param stockCode 股票编码
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "stockCode", value = "股票编码", required = true)
    })
    @ApiOperation(value = "获取指定股票指定T日每分钟交易数据", notes = "获取指定股票指定T日每分钟交易数据", httpMethod = "GET")
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.getStockScreenTimeSharing(stockCode);
    }

    /**
     *  单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "stockCode", value = "股票编码", required = true)
    })
    @ApiOperation(value = "单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据", notes = "单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据", httpMethod = "GET")
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getStockScreenDkLine(@RequestParam(value = "code",required = true) String stockCode){
        return stockService.getStockScreenDkLine(stockCode);
    }

    /**
     *  根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     * @param stockCode  个股代码
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "stockCode", value = "个股代码", required = true)
    })
    @ApiOperation(value = "根据输入的个股代码，进行模糊查询，返回证券代码和证券名称", notes = "根据输入的个股代码，进行模糊查询，返回证券代码和证券名称", httpMethod = "GET")
    @GetMapping("/stock/search")
    public R<List<Map<String,String>>>  getStockSearch(@RequestParam(value ="searchStr") String stockCode){
        return stockService.getStockSearch(stockCode);
    }

    /**
     * 个股主营业务查询
     * @param stockCode 个股代码
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "stockCode", value = "个股代码", required = true)
    })
    @ApiOperation(value = "个股主营业务查询", notes = "个股主营业务查询", httpMethod = "GET")
    @GetMapping("/stock/describe")
    public R<Map<String,String>> getStockDescribe(@RequestParam(value = "code") String stockCode)
    {
        return stockService.getStockDescribe(stockCode);
    }

}
