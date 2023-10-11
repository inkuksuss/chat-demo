package com.example.redispub.config;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

//@Configuration
//@EnableJpaRepositories(basePackages = "com.example.redispub.repository",
//        transactionManagerRef = "jpaTransactionManager")
public class JpaConfig {

    @Autowired
    Environment environment;

    @Autowired
    JpaProperties jpaProperties;

    @Primary
    @Bean(name = "h2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPersistenceUnitName("persistenceUnit");
        emf.setPackagesToScan("com.example.redispub.entity");
        emf.setJpaProperties(additionalProperties());
        emf.setJpaPropertyMap(jpaProperties.getProperties());
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return emf;
    }

//    @Bean
    @Primary
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.setProperty("hibernate.show_sql", environment.getProperty("spring.jpa.show-sql"));
        properties.setProperty("hibernate.format_sql", environment.getProperty("spring.jpa.properties.hibernate.format_sql"));

        return properties;
    }
}
