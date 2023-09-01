//package io.app.arbittrading.serviceEqualsCurrencies;
//
//import io.app.arbittrading.binance.bean.BinanceBean;
//import io.app.arbittrading.binance.service.BinanceService;
//import io.app.arbittrading.cex.bean.CexBean;
//import io.app.arbittrading.cex.service.CexService;
//import io.app.arbittrading.telegram.TelegramSender;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Component
//@AllArgsConstructor
//public class CurrencyDifference {
//    private final CexService cexService;
//    private final BinanceService binanceService;
//    private final TelegramSender telegramSender;
//    private static int countSession = 1;
//    private static final int PERCENTAGE_VALUE = 5;
//
//    @Scheduled(fixedDelay = 30000)
//    @Async
//    public void calculateDifference() {
//        List<CexBean.PriceData> cexCurrencyList = cexService.getCexData().getData();
//        List<BinanceBean> binanceCurrencyList = binanceService.getBinanceData();
//
//        Map<String, BigDecimal> cexData = getDataFromCex(cexCurrencyList);
//        Map<String, BigDecimal> binanceData = getDataFromBinance(binanceCurrencyList);
//
//        comparePricesAndSendMessage(cexData, binanceData);
//
//        incrementAndLogSessionCount();
//    }
//
//    private Map<String, BigDecimal> getDataFromCex(List<CexBean.PriceData> cexCurrencyList) {
//        Map<String, BigDecimal> cexPrices = new HashMap<>();
//        for (CexBean.PriceData cexData : cexCurrencyList) {
//            cexPrices.put(cexData.getSymbol1(), cexData.getLprice());
//        }
//        return cexPrices;
//    }
//
//    private Map<String, BigDecimal> getDataFromBinance(List<BinanceBean> binanceCurrencyList) {
//        Map<String, BigDecimal> binancePrices = new HashMap<>();
//        for (BinanceBean binancePriceData : binanceCurrencyList) {
//            binancePrices.put(binancePriceData.getSymbol(), binancePriceData.getPrice());
//        }
//        return binancePrices;
//    }
//
//    private void comparePricesAndSendMessage(Map<String, BigDecimal> cexData, Map<String, BigDecimal> binanceData) {
//        for (String currency : cexData.keySet()) {
//            if (binanceData.containsKey(currency)) {
//                final BigDecimal cexPrice = cexData.get(currency);
//                final BigDecimal binancePrice = binanceData.get(currency);
//
//                final BigDecimal diffPercent = calculatePriceDifferencePercent(cexPrice, binancePrice);
//
//                if (diffPercent.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0) {
//                    String message = formatPriceDifferenceMessage(currency, diffPercent, cexPrice, binancePrice);
//                    telegramSender.sendMessage(message);
//                }
//            }
//        }
//    }
//
//    private BigDecimal calculatePriceDifferencePercent(BigDecimal cexPrice, BigDecimal binancePrice) {
//        return cexPrice.subtract(binancePrice)
//                .multiply(BigDecimal.valueOf(100))
//                .divide(binancePrice, 2, RoundingMode.HALF_UP);
//    }
//
//    private String formatPriceDifferenceMessage(String currency, BigDecimal diffPercent, BigDecimal cexPrice, BigDecimal binancePrice) {
//        String urlOnBuy = "https://www.binance.com/";
//        String urlOnSell = "https://cex.io/";
//        String message;
//        if (cexPrice.compareTo(binancePrice) > 0) {
//            message = String.format("""
//                            currency %s / USDT: Cex has a higher price by %.2f%%\s
//                            binance price: %s\s
//                            cex price: %s\s
//                            url on sale: %s
//                            url on buy: %s""",
//                    currency, diffPercent.abs(), binancePrice, cexPrice, urlOnSell, urlOnBuy);
//        } else {
//            message = String.format("""
//                            currency %s / USDT: Binance has a higher price by %.2f%%\s
//                            binance price: %s\s
//                            cex price: %s\s
//                            url on sale: %s
//                            url on buy: %s""",
//                    currency, diffPercent.abs(), binancePrice, cexPrice, urlOnSell, urlOnBuy);
//        }
//        return message;
//    }
//
//    private void incrementAndLogSessionCount() {
//        telegramSender.sendMessage("session: " + countSession);
//        log.info("session: " + countSession);
//        countSession++;
//    }
//}
