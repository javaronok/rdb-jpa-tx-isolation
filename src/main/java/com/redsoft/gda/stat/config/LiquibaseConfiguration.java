package com.redsoft.gda.stat.config;

import com.redsoft.gda.liquibase.LiquibaseApplicationListener;
import com.redsoft.gda.liquibase.LiquibaseUpdater;
import com.redsoft.gda.liquibase.rdb.RedDatabase;
import liquibase.database.DatabaseFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter({TargetDataSourceConfig.class})
public class LiquibaseConfiguration {

  @Bean
  public LiquibaseUpdater liquibase(DataSource targetDataSource) {
    DatabaseFactory.getInstance().register(new RedDatabase());
    LiquibaseUpdater liquibase = new LiquibaseUpdater();
    liquibase.setChangeLog("classpath:db/migration/db.master.xml");
    liquibase.setDataSource(targetDataSource);
    return liquibase;
  }

  @Bean
  public LiquibaseApplicationListener liquibaseApplicationListener() {
    return new LiquibaseApplicationListener();
  }
}
