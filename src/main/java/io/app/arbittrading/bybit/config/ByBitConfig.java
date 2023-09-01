package io.app.arbittrading.bybit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ByBitConfig {
    @Value("${bybit.apiKey}")
    protected String apiKey;
    @Value("${bybit.secretKey}")
    protected String secretKey;
    @Value("${bybit.apiBaseUrl}")
    protected String apiBaseUrl;
}
