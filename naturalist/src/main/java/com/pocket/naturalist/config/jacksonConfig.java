package com.pocket.naturalist.config;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class jacksonConfig {

    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }
}