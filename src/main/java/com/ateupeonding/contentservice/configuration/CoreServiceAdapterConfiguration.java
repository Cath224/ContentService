package com.ateupeonding.contentservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CoreServiceAdapterConfiguration {

    @Autowired
    private CoreServiceProperties properties;

    @Bean("coreServiceRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .rootUri(properties.getUrl())
                .build();
    }

}
