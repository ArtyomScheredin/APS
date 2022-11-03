package ru.scheredin;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan
@PropertySource("../../../resources/application.properties")
public class Config {
    @Value("${buffer}")
    public String BUFFER_SIZE;

    @Bean
    @Qualifier("bufferInsertedMonitor")
    public Object bufferInsertedMonitor() {
        return new Object();
    }

}
