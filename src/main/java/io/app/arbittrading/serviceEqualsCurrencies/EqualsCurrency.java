//package io.app.arbittrading.serviceEqualsCurrencies;
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
//public class EqualsCurrency {
//    private final CexService cexService;
//    private final BinanceService binanceService;
//    private final CurrencyService currencyExchangeService;
//    private final TelegramSender telegramSender;
//    private static int countSession = 1;
//    private static final int PERCENTAGE_VALUE = 5;
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
//        Map<String, BigDecimal> currencyExchangeData = getDataFromCurrency(currencyCurrencyList);
//
//        comparePricesAndSendMessage(cexData, binanceData, currencyExchangeData);
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
//
//    private void comparePricesAndSendMessage(Map<String, BigDecimal> cexData, Map<String, BigDecimal> binanceData, Map<String, BigDecimal> currencyData) {
//        for (String currency : cexData.keySet()) {
//            if (binanceData.containsKey(currency)) {
//                if (currencyData.containsKey(currency)) {
//                    final BigDecimal cexPrice = cexData.get(currency);
//                    final BigDecimal binancePrice = binanceData.get(currency);
//                    final BigDecimal currencyPrice = currencyData.get(currency);
//
//                    final BigDecimal diffPercent1 = calculatePriceDifferencePercent(cexPrice, binancePrice);
//                    final BigDecimal diffPercent2 = calculatePriceDifferencePercent(cexPrice, currencyPrice);
//
//                    if (diffPercent1.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0 || diffPercent2.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0) {
//                        String message = formatPriceDifferenceMessage(currency, diffPercent1, diffPercent2, cexPrice, binancePrice, currencyPrice);
//                        telegramSender.sendMessage(message);
//                    }
//                } else {
//                    final BigDecimal cexPrice = cexData.get(currency);
//                    final BigDecimal binancePrice = binanceData.get(currency);
//                    final BigDecimal curruncyPrice = currencyData.get(currency);
//                    final BigDecimal diffPercent = calculatePriceDifferencePercent(cexPrice, binancePrice);
//                    final BigDecimal diffPercent2 = calculatePriceDifferencePercent(cexPrice, curruncyPrice);
//                    if (diffPercent.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0) {
//                        String message = formatPriceDifferenceMessage(currency, diffPercent, diffPercent2, cexPrice, binancePrice, curruncyPrice);
//                        telegramSender.sendMessage(message);
//                    }
//                }
//            }
//        }
//    }
//
//    private BigDecimal calculatePriceDifferencePercent(BigDecimal price1, BigDecimal price2) {
//        return price1.subtract(price2)
//                .multiply(BigDecimal.valueOf(100))
//                .divide(price2, 2, RoundingMode.HALF_UP);
//    }
//
//    private String formatPriceDifferenceMessage(String currency, BigDecimal diffPercent1, BigDecimal diffPercent2, BigDecimal cexPrice, BigDecimal binancePrice, BigDecimal currencyPrice) {
//        String urlOnBuy = "https://www.binance.com/";
//        String urlOnSell = "https://cex.io/";
//        String message;
//        if (cexPrice.compareTo(binancePrice) > 0 && cexPrice.compareTo(currencyPrice) > 0) {
//            message = String.format("""
//                        currency %s / USDT: Cex has a higher price by %.2f%% compared to Binance and %.2f%% compared to Currency\s
//                        binance price: %s\s
//                        cex price: %s\s
//                        currency price: %s\s
//                        url on sale: %s
//                        url on buy: %s""",
//                    currency, diffPercent1.abs(), diffPercent2.abs(), binancePrice, cexPrice, currencyPrice,
//                    urlOnSell, urlOnBuy);
//        } else if (binancePrice.compareTo(cexPrice) > 0 && binancePrice.compareTo(currencyPrice) > 0) {
//            message = String.format("""
//                            currency %s / USDT: Binance has a higher price by %.2f%% compared to Cex and %.2f%% compared to Currency\s
//                            binance price: %s\s
//                            cex price: %s\s
//                            currency price: %s\s
//                            url on sale: %s
//                            url on buy: %s""",
//                    currency, diffPercent1.abs(), diffPercent2.abs(), binancePrice, cexPrice, currencyPrice,
//                    urlOnSell, urlOnBuy);
//        }
//        else {
//            return message = String.format("""
//                            currency %s / USDT: Cex has a higher price by %.2f%% compared to Currency and %.2f%% compared to Binance\s
//                            binance price: %s\s
//                            cex price: %s\s
//                            currency price: %s\s
//                            url on sale: %s
//                            url on buy: %s""",
//                    currency, diffPercent1.abs(), diffPercent2.abs(), binancePrice, cexPrice, currencyPrice,
//                    urlOnSell, urlOnBuy);
//        }
//   return message;
//    }
//    private void incrementAndLogSessionCount() {
//        telegramSender.sendMessage("session: " + countSession);
//        log.info("session: " + countSession);
//        countSession++;
//    }
//}
