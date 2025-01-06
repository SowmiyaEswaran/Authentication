package com.auth.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "es.twilio")
@Data
public class TwilioConfig {
    private String accountSid;
    private String authToken;
    private String phoneNumber;
    private Duration expireDuration;
    private String message1;
    private String message2;
}
