package com.ozzydemo.cdc.source.wordpress;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableJpaRepositories(basePackages = {"com.ozzydemo.cdc.commons.data", "com.ozzydemo.cdc.source"})
@EnableJpaRepositories
@EntityScan(basePackages = {"com.ozzydemo.cdc.commons.data", "com.ozzydemo.cdc.source"})
@EnableTransactionManagement
public class WordpressReaderConfig
{
}
