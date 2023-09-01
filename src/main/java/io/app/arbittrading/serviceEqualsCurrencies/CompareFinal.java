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
//public class CompareFinal {
//    private final CexService cexService;
//    private final BinanceService binanceService;
//    private final CurrencyService currencyExchangeService;
//    private final TelegramSender telegramSender;
//    private static int countSession = 1;
//    private static final int PERCENTAGE_VALUE = 5;
//    Valuta valuta;
//    List<Valuta> valutas;
//    private static Set<String> allNameCurrency = new HashSet<>();
//    static
//    List<String> exchangeName = new ArrayList<>();
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
//        Map<String, Map<String, BigDecimal>> allExchanges = new HashMap<>();
//        allExchanges.put("cex", cexData);
//        allExchanges.put("binance", binanceData);
//        allExchanges.put("currency", currencyExchangeData);
//
//
//        for (Map.Entry<String, Map<String, BigDecimal>> entry : allExchanges.entrySet()) {
//            exchangeName.add(entry.getKey());
//        }
//        comparePricesAndSendMessage(allExchanges);
//        //comparePricesAndSendMessage(cexData, binanceData, currencyExchangeData);
//
//        incrementAndLogSessionCount();
//    }
//
//    private Map<String, BigDecimal> getDataFromCex(List<CexBean.PriceData> cexCurrencyList) {
//        Map<String, BigDecimal> cexPrices = new HashMap<>();
//        for (CexBean.PriceData cexData : cexCurrencyList) {
//            cexPrices.put(cexData.getSymbol1(), cexData.getLprice());
//            allNameCurrency.add(cexData.getSymbol1());
//        }
//        return cexPrices;
//    }
//
//    private Map<String, BigDecimal> getDataFromBinance(List<BinanceBean> binanceCurrencyList) {
//        Map<String, BigDecimal> binancePrices = new HashMap<>();
//        for (BinanceBean binancePriceData : binanceCurrencyList) {
//            binancePrices.put(binancePriceData.getSymbol(), binancePriceData.getPrice());
//            allNameCurrency.add(binancePriceData.getSymbol());
//        }
//        return binancePrices;
//    }
//
//    private Map<String, BigDecimal> getDataFromCurrency(List<CurrencyBean> currencyExchangeList) {
//        Map<String, BigDecimal> currencyExchangePrices = new HashMap<>();
//        for (CurrencyBean currencyExchangeData : currencyExchangeList) {
//            currencyExchangePrices.put(currencyExchangeData.getSymbol(), currencyExchangeData.getLastPrice());
//            allNameCurrency.add(currencyExchangeData.getSymbol());
//        }
//        return currencyExchangePrices;
//    }
//
//    private void comparePricesAndSendMessage(Map<String, Map<String, BigDecimal>> allExchanges) {
//
//        for (String name : allNameCurrency) {
//            valuta = new Valuta();
//            valuta.exchangeNameAndPrice = new HashMap<>();
//            for (String exchange : exchangeName) {
//                valuta.exchangeNameAndPrice.put(exchange, allExchanges.get(exchange).get(name));
//            }
//                valuta.nameValuta = name;
//                valutas.add(valuta);
//        }
//        BigDecimal cexPrice;
//        BigDecimal binancePrice;
//        BigDecimal currencyPrice;
//
//        for (Valuta valuta : valutas) {
//            for (String name : allNameCurrency) {
//                if (valuta.nameValuta.equals(name)) {
//                    cexPrice = valuta.exchangeNameAndPrice.get(exchangeName.get(0));
//                    binancePrice = valuta.exchangeNameAndPrice.get(exchangeName.get(1));
//                    currencyPrice = valuta.exchangeNameAndPrice.get(exchangeName.get(3));
//                    log.info(valuta.nameValuta + "/ USDT");
//                    log.info("cex price" + cexPrice);
//                    log.info("binance price" + binancePrice);
//                    log.info("currency price" + currencyPrice);
//                }
//            }
//        }
//
//        Set<String> processedCurrencies = new HashSet<>();
//        for (String currencyBinance : binanceData.keySet()) {
//            for (String currencyCex : cexData.keySet()) {
//                if (cexData.containsKey(currencyBinance) || currencyData.containsKey(currencyBinance) ||
//                        currencyData.containsKey(currencyCex)) {
//                    //  if (currencyData.get(currencyBinance) != null || cexData.get(currencyBinance) != null) {
//                    final BigDecimal cexPrice = cexData.get(currencyBinance);
//                    final BigDecimal binancePrice = binanceData.get(currencyBinance);
//                    final BigDecimal currencyPrice = currencyData.get(currencyBinance);
//
//                    final BigDecimal diffPercentCexBinance = calculatePriceDifferencePercent(cexPrice, binancePrice);
//                    final BigDecimal diffPercentCexCurrency = calculatePriceDifferencePercent(cexPrice, currencyPrice);
//                    final BigDecimal diffPercentBinanceCurrency = calculatePriceDifferencePercent(binancePrice, currencyPrice);
//
//                    boolean isNewCurrency = processedCurrencies.add(currencyBinance);
//                    if (isNewCurrency && (
//                            diffPercentCexBinance.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0 ||
//                                    diffPercentCexCurrency.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0 ||
//                                    diffPercentBinanceCurrency.abs().compareTo(BigDecimal.valueOf(PERCENTAGE_VALUE)) > 0)) {
//                        formatPriceDifferenceMessage(currencyBinance, diffPercentCexBinance, diffPercentCexCurrency, diffPercentBinanceCurrency, cexPrice, binancePrice, currencyPrice);
//                        telegramSender.sendMessage(formatPriceDifferenceMessage(currencyBinance, diffPercentCexBinance, diffPercentCexCurrency, diffPercentBinanceCurrency, cexPrice, binancePrice, currencyPrice));
//
//                    }
//                }
//            }
//        }
//    }
//
//    private BigDecimal calculatePriceDifferencePercent(BigDecimal price1, BigDecimal price2) {
//        if (price1 != null & price2 != null) {
//            return price1.subtract(price2)
//                    .multiply(BigDecimal.valueOf(100))
//                    .divide(price2, 2, RoundingMode.HALF_UP);
//        } else {
//            // throw new RuntimeException();
//            return new BigDecimal(0);
//        }
//    }
//
//    private String formatPriceDifferenceMessage(String currency, BigDecimal diffPercentCexBinance, BigDecimal diffPercentCexCurrency, BigDecimal diffPercentBinanceCurrency, BigDecimal cexPrice, BigDecimal binancePrice, BigDecimal currencyPrice) {
//        String urlOnBuy = "https://www.binance.com/";
//        String urlOnSell = "https://cex.io/";
//        String message = String.format("""
//                        currency %s / USDT:\s
//                        Cex - Binance difference: %.2f%%\s
//                        Cex - Currency difference: %.2f%%\s
//                        Binance - Currency difference: %.2f%%\s
//                        binance price: %s\s
//                        cex price: %s\s
//                        currency price: %s\s
//                        url on sale: %s
//                        url on buy: %s""",
//                currency, diffPercentCexBinance.abs(), diffPercentCexCurrency.abs(), diffPercentBinanceCurrency.abs(), binancePrice, cexPrice, currencyPrice, urlOnSell, urlOnBuy);
//        return message;
//    }
//
//    private void incrementAndLogSessionCount() {
//        telegramSender.sendMessage("session: " + countSession);
//        log.info("session: " + countSession);
//        countSession++;
//    }
//
//    @Component
//    static class Valuta {
//        String nameValuta;
//        Map<String, BigDecimal> exchangeNameAndPrice = new HashMap<>();
//
//        @Override
//        public String toString() {
//            return "Valuta{" +
//                    "nameValuta='" + nameValuta + '\'' +
//                    ", exchangeNameAndPrice=" + exchangeNameAndPrice +
//                    '}';
//        }
//    }
//}

