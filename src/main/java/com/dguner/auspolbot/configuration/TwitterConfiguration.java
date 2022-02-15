package com.dguner.auspolbot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twitter")
public class TwitterConfiguration {
    private String accessToken;
    private String accessTokenSecret;
    private String apiKey;
    private String apiSecretKey;
    private boolean shouldTweet;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecretKey() {
        return apiSecretKey;
    }

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
    }

    public boolean isShouldTweet() {
        return shouldTweet;
    }

    public void setShouldTweet(boolean shouldTweet) {
        this.shouldTweet = shouldTweet;
    }
}
