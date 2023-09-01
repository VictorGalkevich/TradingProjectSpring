package io.app.arbittrading.bybit.client;

import io.app.arbittrading.bybit.bean.ByBitBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "byBitFeignClient", url = "${bybit.apiBaseUrl}")
public interface ByBitFeignClient {
    @GetMapping("/spot/v3/public/symbols")
    List<ByBitBean> getByBitData();
}
