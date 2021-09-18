package com.bzaja.ljudskiresursiiposlovniprocesilibrary.config;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.util.Properties;
import java.util.Set;
import javax.persistence.Entity;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

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
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }

    @Bean
    public Set<BeanDefinition> getBeanDefinitionsByEntityAnnotation() {
        ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider
                = new ClassPathScanningCandidateComponentProvider(false);

        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        return classPathScanningCandidateComponentProvider.findCandidateComponents("com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature");
    }
}
