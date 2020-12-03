package com.bzaja.ljudskiresursiiposlovniprocesilibrary.config;

import com.bzaja.myjavalibrary.springframework.factory.HibernateValidatorFactory;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import javax.validation.Validator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.bzaja.ljudskiresursiiposlovniprocesilibrary")
@EnableJpaRepositories(value = "com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature")
public class AppConfig {

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public DataSource dataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("LjudskiResursiIPoslovniProcesi");
        dataSource.setUser("sa");
        dataSource.setPassword("SQL");
        dataSource.setPortNumber(1433);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServer2008Dialect");
        return hibernateProperties;
    }

    @Bean
    public Validator getValidator(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        return HibernateValidatorFactory.get(autowireCapableBeanFactory);
    }
}
