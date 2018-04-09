package com.redsoft.gda.stat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = "com.redsoft.gda.stat",
        entityManagerFactoryRef = "targetEntityManager",
        transactionManagerRef = "targetTransactionManager"
)
public class TargetDataSourceConfig {
  private final JpaProperties jpaProps;
  private final DataSourceProperties dataSourceProperties;

  @Bean
  public LocalContainerEntityManagerFactoryBean targetEntityManager() {
    LocalContainerEntityManagerFactoryBean em
            = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(targetDataSource());
    em.setPackagesToScan("com.redsoft.gda.stat");

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", jpaProps.getHibernate().getDdlAuto());
    properties.put("hibernate.dialect", jpaProps.getDatabasePlatform());
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Bean
  public DataSource targetDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
    dataSource.setUrl(dataSourceProperties.getUrl());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());
    return dataSource;
  }

  @Bean
  public PlatformTransactionManager targetTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(targetEntityManager().getObject());
    return transactionManager;
  }
}
