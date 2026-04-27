package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

@Configuration
    @EnableConfigurationProperties({PaymentDbProperties.class, PaymentDbLiquibaseProperties.class})
    @Qualifier("paymentDbEntityManager")
    @EnableJpaRepositories(
            basePackages = "com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository",
            entityManagerFactoryRef = "paymentDbEntityManager",
            transactionManagerRef = "paymentDbTransactionManager")

public class PaymentDbConfig {
    private final PaymentDbProperties paymentDbProperties;
    private final PaymentDbLiquibaseProperties paymentDbLiquibaseProperties;

    public PaymentDbConfig(PaymentDbProperties paymentDbProperties, PaymentDbLiquibaseProperties paymentDbLiquibaseProperties) {
        this.paymentDbProperties = paymentDbProperties;
        this.paymentDbLiquibaseProperties = paymentDbLiquibaseProperties;
    }

    @Bean(name = "paymentDbEntityManager")
    public LocalContainerEntityManagerFactoryBean paymentDbEntitymanager() throws SQLException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", paymentDbProperties.getHibernateProperties().getHb2ddl());
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", paymentDbProperties.getHibernateProperties().isShowSql());
        properties.put("hibernate.format_sql", paymentDbProperties.getHibernateProperties().isFormatSql());
        properties.put("hibernate.default_batch_fetch_size", 500);
        properties.put("hibernate.jdbc.batch_size", 100);
        properties.put("hibernate.order_inserts", true);
        properties.put("hibernate.order_updates", true);
        properties.put("hibernate.connection.release_mode", "after_transaction");
        em.setDataSource(paymentDbDataSource());
        em.setPackagesToScan("com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "paymentDbTransactionManager")
    @Primary
    public PlatformTransactionManager paymentDbTransactionManager(
            @Qualifier("paymentDbEntityManager") LocalContainerEntityManagerFactoryBean paymentDbEntityManager) {
        return new JpaTransactionManager(Objects.requireNonNull(paymentDbEntityManager.getObject()));
    }


    @Bean(name = "paymentDbTransactionTemplate")
    @Primary
    public TransactionTemplate paymentDbTransactionTemplate(
            @Qualifier("paymentDbTransactionManager") PlatformTransactionManager paymentDbTransactionManager) {
        return new TransactionTemplate(paymentDbTransactionManager);

    }

    @Bean(name = "paymentDbDataSource")
    @Primary
    public DataSource paymentDbDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(paymentDbProperties.getDbURL())
                .username(paymentDbProperties.getDbUsername())
                .password(paymentDbProperties.getDbPassword())
                .build();
    }

    @Bean(name = "paymentDbLiquibaseDataSource")
    public DataSource paymentDbLiquibaseDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(paymentDbProperties.getDbURL())
                .username(paymentDbProperties.getDbUsername())
                .password(paymentDbProperties.getDbPassword())
                .build();
    }

//    @Bean(name = "paymentDbLiquibase")
//    public SpringLiquibase paymentDbLiquibase(
//            @Qualifier("paymentDbLiquibaseDataSource") DataSource paymentDbLiquibaseDataSource) {
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setChangeLog("");
//        liquibase.setDataSource(paymentDbLiquibaseDataSource);
//        return liquibase;
//    }

}
