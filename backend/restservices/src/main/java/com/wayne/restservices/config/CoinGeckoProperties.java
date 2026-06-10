package com.wayne.restservices.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "coingecko.api")
public class CoinGeckoProperties {

    private String key;
    private String url;
    private String defaultCurrency;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }
    public void setDefaultCurrency(String defaultCurrency) { this.defaultCurrency = defaultCurrency; }
    public String getDefaultCurrency() { return defaultCurrency; }
}
