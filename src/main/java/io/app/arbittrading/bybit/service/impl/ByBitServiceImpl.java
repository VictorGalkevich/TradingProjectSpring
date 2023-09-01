package io.app.arbittrading.bybit.service.impl;

import io.app.arbittrading.binance.client.BinanceFeignClient;
import io.app.arbittrading.bybit.bean.ByBitBean;
import io.app.arbittrading.bybit.client.ByBitFeignClient;
import io.app.arbittrading.bybit.service.ByBitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ByBitServiceImpl implements ByBitService {

    private final ByBitFeignClient byBitFeignClient;
    @Override
    public List<ByBitBean> getByBitData() {
        try {
            List<ByBitBean> allCurrencies = byBitFeignClient.getByBitData();
            List<ByBitBean> currenciesEndWithUSDT = new ArrayList<>();
            for (ByBitBean byBitBean : allCurrencies) {
                if (byBitBean.getSymbol().endsWith("USDT")) {
                    String symbol = byBitBean.getSymbol().replace("USDT", "");
                    BigDecimal price = byBitBean.getPrice();

                    ByBitBean filteredBinanceBean = new ByBitBean(symbol, price);
                    currenciesEndWithUSDT.add(filteredBinanceBean);
                }
            }
            return currenciesEndWithUSDT;
        } catch (Exception e) {
            log.error("An error occurred while getting ticker prices", e);
            throw new RuntimeException("error occurred currencies");
        }
    }
}
