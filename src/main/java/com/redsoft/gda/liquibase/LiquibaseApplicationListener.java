package com.redsoft.gda.liquibase;

import liquibase.exception.LiquibaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class LiquibaseApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private LiquibaseUpdater updater;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            updater.update();
        } catch (LiquibaseException e) {
            throw new RuntimeException("Update DB error", e);
        }
    }
}
