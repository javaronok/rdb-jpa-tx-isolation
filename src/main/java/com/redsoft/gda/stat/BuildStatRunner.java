package com.redsoft.gda.stat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Component
public class BuildStatRunner implements CommandLineRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(BuildStatRunner.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ApplicationContext ctx;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Started...");

        runWithReadCommitted();
        runWithRepeatableRead();

        taskExecutor.shutdown();
        LOGGER.info("shutdown.");
    }

    public void runWithReadCommitted() throws ExecutionException, InterruptedException {
        WorkSyncConstraints constraints = new WorkSyncConstraints();
        AddNewRecordProcessor addNewRecordProcessor = (AddNewRecordProcessor)
                ctx.getBean("AddNewRecordProcessor", constraints);
        GenericFetchDataProcessor fetchDataProcessor = (GenericFetchDataProcessor)
                ctx.getBean("GenericFetchDataProcessor", constraints, Isolation.READ_COMMITTED);

        taskExecutor.submit(addNewRecordProcessor);
        Future f = taskExecutor.submit(fetchDataProcessor);
        f.get();
    }

    public void runWithRepeatableRead() throws ExecutionException, InterruptedException {
        WorkSyncConstraints constraints = new WorkSyncConstraints();
        AddNewRecordProcessor addNewRecordProcessor = (AddNewRecordProcessor)
                ctx.getBean("AddNewRecordProcessor", constraints);
        GenericFetchDataProcessor fetchDataProcessor = (GenericFetchDataProcessor)
                ctx.getBean("GenericFetchDataProcessor", constraints, Isolation.REPEATABLE_READ);

        taskExecutor.submit(addNewRecordProcessor);
        Future f = taskExecutor.submit(fetchDataProcessor);
        f.get();
    }
}
