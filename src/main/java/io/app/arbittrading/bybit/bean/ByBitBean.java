package io.app.arbittrading.bybit.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ByBitBean {
    private String symbol;
    private BigDecimal price;
}
