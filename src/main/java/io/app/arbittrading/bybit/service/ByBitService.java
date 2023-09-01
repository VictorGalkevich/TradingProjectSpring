package io.app.arbittrading.bybit.service;

import io.app.arbittrading.binance.bean.BinanceBean;
import io.app.arbittrading.bybit.bean.ByBitBean;

import java.util.List;

public interface ByBitService {
    List<ByBitBean> getByBitData();
}
