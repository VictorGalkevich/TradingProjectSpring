package io.app.arbittrading.binance.client;

import io.app.arbittrading.binance.model.BinanceTickerPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "binanceFeignClient", url = "${binance.apiBaseUrl}")
public interface BinanceFeignClient {
    @GetMapping("/api/v3/ticker/price")
    List<BinanceTickerPrice> getTickerPrices();

}
