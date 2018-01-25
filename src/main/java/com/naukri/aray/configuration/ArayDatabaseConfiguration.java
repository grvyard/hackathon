package com.naukri.aray.configuration;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "arayEntityManagerFactory", 
        basePackages = { "com.naukri.aray.repository" })
public class ArayDatabaseConfiguration {

    
    @Bean(name = "arayDataSource")
    @ConfigurationProperties(prefix="aray.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    
    @Bean(name = "arayEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("arayDataSource") DataSource arayDataSource) {
        return builder
                .dataSource(arayDataSource)
                .packages("com.naukri.aray.model")
                .persistenceUnit("aray")
                .build();
    }

}