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
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//@AllArgsConstructor
//public class CompareCurrency {
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
//
//        for (String currency1 : cexData.keySet()) {
//            for (String currency2 : binanceData.keySet()) {
//                for (String currency3 : currencyData.keySet()) {
//                    if (currency1.equals(currency2) || currency1.equals(currency3) || currency2.equals(currency3)) {
//                        continue; // Skip if any currency is the same
//                    }
//
//                    final BigDecimal cexPrice = cexData.get(currency1);
//                    final BigDecimal binancePrice = binanceData.get(currency2);
//                    final BigDecimal currencyPrice = currencyData.get(currency3);
//
//                    final BigDecimal diffPercentCexBinance = calculatePriceDifferencePercent(cexPrice, binancePrice);
//                    final BigDecimal diffPercentCexCurrency = calculatePriceDifferencePercent(cexPrice, currencyPrice);
//                    final BigDecimal diffPercentBinanceCurrency = calculatePriceDifferencePercent(binancePrice, currencyPrice);
//
//                    boolean isNewCurrency = processedCurrencies.add(currency1 + "-" + currency2 + "-" + currency3);
//                    if (isNewCurrency && (
//                            diffPercentCexBinance.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0 ||
//                                    diffPercentCexCurrency.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0 ||
//                                    diffPercentBinanceCurrency.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0)) {
//                        String message = formatPriceDifferenceMessage(currency1, currency2, currency3, diffPercentCexBinance, diffPercentCexCurrency, diffPercentBinanceCurrency, cexPrice, binancePrice, currencyPrice);
//                        telegramSender.sendMessage(message);
//                    }
//                }
//            }
//        }
//    }
//
//    private BigDecimal calculatePriceDifferencePercent(BigDecimal price1, BigDecimal price2) {
//        return price1.subtract(price2).multiply(BigDecimal.valueOf(100)).divide(price2, 2, RoundingMode.HALF_UP);
//    }
//
//    private String formatPriceDifferenceMessage(String currency1, String currency2, String currency3, BigDecimal diffPercentCexBinance, BigDecimal diffPercentCexCurrency, BigDecimal diffPercentBinanceCurrency, BigDecimal cexPrice, BigDecimal binancePrice, BigDecimal currencyPrice) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Price difference alert! ");
//        sb.append(currency1).append("-").append(currency2).append("-").append(currency3);
//        sb.append(":\n");
//
//        if (diffPercentCexBinance.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0) {
//            sb.append("CEX price: ").append(cexPrice);
//            sb.append(", Binance price: ").append(binancePrice);
//            sb.append(", price difference: ").append(diffPercentCexBinance).append("%");
//            sb.append("\n");
//        }
//
//        if (diffPercentCexCurrency.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0) {
//            sb.append("CEX price: ").append(cexPrice);
//            sb.append(", Currency price: ").append(currencyPrice);
//            sb.append(", price difference: ").append(diffPercentCexCurrency).append("%");
//            sb.append("\n");
//        }
//
//        if (diffPercentBinanceCurrency.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0) {
//            sb.append("Binance price: ").append(binancePrice);
//            sb.append(", Currency price: ").append(currencyPrice);
//            sb.append(", price difference: ").append(diffPercentBinanceCurrency).append("%");
//            sb.append("\n");
//        }
//
//        return sb.toString();
//    }
//
//    private void incrementAndLogSessionCount() {
//        telegramSender.sendMessage("session: " + countSession);
//        log.info("session: " + countSession);
//        countSession++;
//    }
//}