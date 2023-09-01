//package io.app.arbittrading.serviceEqualsCurrencies.comparisonOfTwoExchanges;
//
//import io.app.arbittrading.binance.bean.BinanceBean;
//import io.app.arbittrading.binance.service.BinanceService;
//import io.app.arbittrading.cex.bean.CexBean;
//import io.app.arbittrading.cex.service.CexService;
//import io.app.arbittrading.currency.bean.CurrencyBean;
//import io.app.arbittrading.currency.service.CurrencyService;
//import io.app.arbittrading.telegram.TelegramSender;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.*;
//
//@Slf4j
//@Component
//@AllArgsConstructor
//public class CompareTwoExchanges {
//    private final CexService cexService;
//    private final BinanceService binanceService;
//    private final CurrencyService currencyExchangeService;
//    private final TelegramSender telegramSender;
//    private static int countSession = 1;
//    private static final int PERCENTAGE_VALUE = 1;
//
//    @Scheduled(fixedDelay = 30000)
//    @Async
//    public void calculateDifference() {
//        List<CexBean.PriceData> cexCurrencyList = cexService.getCexData().getData();
//        List<BinanceBean> binanceCurrencyList = binanceService.getBinanceData();
//        List<CurrencyBean> currencyCurrencyList = currencyExchangeService.getCurrencyData();
//
//        Map<String, BigDecimal> cexData = getDataFromCex(cexCurrencyList);
//        Map<String, BigDecimal> binanceData = getDataFromBinance(binanceCurrencyList);
//
//        comparePricesAndSendMessage2(cexData, binanceData);
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
//    private Map<String, BigDecimal> getDataFromCurrency(List<CurrencyBean> currencyExchangeList) {
//        Map<String, BigDecimal> currencyExchangePrices = new HashMap<>();
//        for (CurrencyBean currencyExchangeData : currencyExchangeList) {
//            currencyExchangePrices.put(currencyExchangeData.getSymbol(), currencyExchangeData.getLastPrice());
//        }
//        return currencyExchangePrices;
//    }
//    private void comparePricesAndSendMessage2(Map<String, BigDecimal> cexData, Map<String, BigDecimal> binanceData) {
//        for (String currency : cexData.keySet()) {
//            if (binanceData.containsKey(currency)) {
//                final BigDecimal cexPrice = cexData.get(currency);
//                final BigDecimal binancePrice = binanceData.get(currency);
//
//                final BigDecimal diffPercent = calculatePriceDifferencePercent(cexPrice, binancePrice);
//
//                if (diffPercent.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0) {
//                    String message = formatPriceDifferenceMessage2(currency, diffPercent, cexPrice, binancePrice);
//                    telegramSender.sendMessage(message);
//                }
//            }
//        }
//    }
//    private BigDecimal calculatePriceDifferencePercent(BigDecimal price1, BigDecimal price2) {
//        return price1.subtract(price2)
//                .multiply(BigDecimal.valueOf(100))
//                .divide(price2, 2, RoundingMode.HALF_UP);
//    }
//
//    private String formatPriceDifferenceMessage2(String currency, BigDecimal diffPercentCexBinance, BigDecimal cexPrice, BigDecimal binancePrice) {
//        String urlOnBuy = "https://www.binance.com/";
//        String urlOnSell = "https://cex.io/";
//        String message = String.format("""
//                    currency %s / USDT:\s
//                    Cex - Binance difference: %.2f%%\s
//                    binance price: %s\s
//                    cex price: %s\s
//                    url on sale: %s
//                    url on buy: %s""",
//                currency, diffPercentCexBinance.abs(), binancePrice, cexPrice, urlOnSell, urlOnBuy);
//        return message;
//    }
//    private void incrementAndLogSessionCount() {
//        telegramSender.sendMessage("session: " + countSession);
//        log.info("session: " + countSession);
//        countSession++;
//    }
//}
//
//
