package com.cai.stock.face;

import java.util.List;

public interface StockCacheFace {
    /**
     * 获取所有股票编码，并添加上证或者深证的股票前缀编号：sh sz
     * @return
     */
    List<String> getAllStockCodeWithPredix();
}
