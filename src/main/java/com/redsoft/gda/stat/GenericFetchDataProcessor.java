package com.redsoft.gda.stat;

import com.redsoft.gda.stat.model.StatData;
import com.redsoft.gda.stat.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Scope("prototype")
@Component("GenericFetchDataProcessor")
public class GenericFetchDataProcessor implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(GenericFetchDataProcessor.class);

    private final WorkSyncConstraints constraints;
    private final Isolation isolationLevel;

    @Autowired
    private StatisticsService service;

    @Autowired
    private TransactionTemplate tx;

    @Autowired
    public GenericFetchDataProcessor(WorkSyncConstraints constraints, Isolation isolationLevel) {
        this.constraints = constraints;
        this.isolationLevel = isolationLevel;
    }

    @Override
    public void run() {
        tx.setReadOnly(true);
        tx.setIsolationLevel(isolationLevel.value());
        tx.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                LOGGER.info(isolationLevel + " isolation");
                constraints.lock();
                List<StatData> data = service.findAll();
                constraints.doneFirstFetchCondition();
                LOGGER.info("Fetched data with " + data.size() + " records, wait ...");
                constraints.awaitAddNewRecordCondition();
                data = service.findAll();
                LOGGER.info("Fetched data with " + data.size() + " records, finish");
                constraints.unlock();
            }
        });
    }
}
