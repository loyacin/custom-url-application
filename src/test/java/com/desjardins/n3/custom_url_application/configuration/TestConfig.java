package com.desjardins.n3.custom_url_application.configuration;

import com.desjardins.n3.custom_url_application.service.CustomUrlService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public CustomUrlService mockCustomUrlService() {
        return Mockito.mock(CustomUrlService.class);
    }
}
