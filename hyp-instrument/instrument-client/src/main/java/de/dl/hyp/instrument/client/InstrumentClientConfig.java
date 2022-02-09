package de.dl.hyp.instrument.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "de.dl.hyp.instrument.client")
@ComponentScan("de.dl.hyp.instrument")
public class InstrumentClientConfig {}
