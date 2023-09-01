//package io.app.arbittrading.serviceEqualsCurrencies;

import io.app.arbittrading.binance.bean.BinanceBean;
import io.app.arbittrading.binance.service.BinanceService;
import io.app.arbittrading.cex.bean.CexBean;
import io.app.arbittrading.cex.service.CexService;
import io.app.arbittrading.currency.bean.CurrencyBean;
import io.app.arbittrading.currency.service.CurrencyService;
import io.app.arbittrading.telegram.TelegramSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

//@Slf4j
//@Component
//@AllArgsConstructor
//public class Compare {
//    private final CexService cexService;
//    private final BinanceService binanceService;
//    private final CurrencyService currencyExchangeService;
//    private final TelegramSender telegramSender;
//    private static int countSession = 1;
//    private static final int PERCENTAGE_VALUE = 1;
//    private static final String URL_ON_BUY = "https://www.binance.com/";
//    private static final String URL_ON_SELL = "https://cex.io/";
//
//    @Scheduled(fixedDelay = 30000)
//    @Async
//    public void calculateDifference() {
//        Map<String, BigDecimal> cexData = getDataFromCex(cexService.getCexData().getData());
//        Map<String, BigDecimal> binanceData = getDataFromBinance(binanceService.getBinanceData());
//        Map<String, BigDecimal> currencyExchangeData = getDataFromCurrency(currencyExchangeService.getCurrencyData());
//
//        comparePricesAndSendMessage(cexData, binanceData, currencyExchangeData);
//
//        incrementAndLogSessionCount();
//    }
//
//    private Map<String, BigDecimal> getDataFromCex(List<CexBean.PriceData> cexCurrencyList) {
//        return cexCurrencyList.stream().collect(Collectors.toMap(CexBean.PriceData::getSymbol1, CexBean.PriceData::getLprice));
//    }
//
//    private Map<String, BigDecimal> getDataFromBinance(List<BinanceBean> binanceCurrencyList) {
//        return binanceCurrencyList.stream().collect(Collectors.toMap(BinanceBean::getSymbol, BinanceBean::getPrice));
//    }
//
//    private Map<String, BigDecimal> getDataFromCurrency(List<CurrencyBean> currencyExchangeList) {
//        return currencyExchangeList.stream().collect(Collectors.toMap(CurrencyBean::getSymbol, CurrencyBean::getLastPrice));
//    }
//
//    private void comparePricesAndSendMessage(Map<String, BigDecimal> cexData, Map<String, BigDecimal> binanceData, Map<String, BigDecimal> currencyData) {
//        Set<String> processedCurrencies = new HashSet<>();
//        cexData.keySet().stream().filter(binanceData::containsKey).filter(currencyData::containsKey)
//                .forEach(currency -> {
//                    final BigDecimal cexPrice = cexData.get(currency);
//                    final BigDecimal binancePrice = binanceData.get(currency);
//                    final BigDecimal currencyPrice = currencyData.get(currency);
//
//                    final BigDecimal diffPercentCexBinance = calculatePriceDifferencePercent(cexPrice, binancePrice);
//                    final BigDecimal diffPercentCexCurrency = calculatePriceDifferencePercent(cexPrice, currencyPrice);
//                    final BigDecimal diffPercentBinanceCurrency = calculatePriceDifferencePercent(binancePrice, currencyPrice);
//
//                    boolean isNewCurrency = processedCurrencies.add(currency);
//                    if (isNewCurrency && (
//                            diffPercentCexBinance.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0 ||
//                                    diffPercentCexCurrency.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0 ||
//                                    diffPercentBinanceCurrency.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0)) {
//                        String message = formatPriceDifferenceMessage(currency, diffPercentCexBinance, diffPercentCexCurrency, diffPercentBinanceCurrency, cexPrice, binancePrice, currencyPrice);
//                        telegramSender.sendMessage(message);
//                    }
//                });
//    }
//
//    private BigDecimal calculatePriceDifferencePercent(BigDecimal price1, BigDecimal price2) {
//        return price1.subtract(price2).multiply(BigDecimal.valueOf(100)).divide(price2, 2, RoundingMode.HALF_UP);
//    }
//
//    private String formatPriceDifferenceMessage(String currency, BigDecimal diffPercentCexBinance, BigDecimal diffPercentCexCurrency, BigDecimal diffPercentBinanceCurrency, BigDecimal cexPrice, BigDecimal binancePrice, BigDecimal currencyPrice) {
//        return String.format("currency %s / USDT:%nCex - Binance difference: %.2f%%%nCex - Currency difference: %.2f%%%nBinance - Currency difference: %.2f%%%nbinance price: %s%ncex price: %s%ncurrency price: %s%nurl on sale: https://cex.io/%nurl on buy: https://www.binance.com/", currency, diffPercentCexBinance.abs(), diffPercentCexCurrency.abs(), diffPercentBinanceCurrency.abs(), binancePrice, cexPrice, currencyPrice);
//    }
//    private void incrementAndLogSessionCount() {
//        telegramSender.sendMessage("session: " + countSession);
//        log.info("session: " + countSession);
//        countSession++;
//    }
//}