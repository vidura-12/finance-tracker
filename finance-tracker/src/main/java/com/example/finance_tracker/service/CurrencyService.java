package com.example.finance_tracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyService {

    @Value("${exchanger.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    private final RestTemplate restTemplate;

    // Constructor-based dependency injection (optional but recommended)
    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String url = BASE_URL + apiKey + "/latest/" + fromCurrency;

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("conversion_rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("conversion_rates");
                if (rates.containsKey(toCurrency)) {
                    return new BigDecimal(rates.get(toCurrency).toString());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rate for " + fromCurrency + " to " + toCurrency, e);
        }

        throw new RuntimeException("Exchange rate not found for " + fromCurrency + " to " + toCurrency);
    }

    public BigDecimal convertAmount(BigDecimal amount, String fromCurrency, String toCurrency) {
        BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(exchangeRate);
    }

}
