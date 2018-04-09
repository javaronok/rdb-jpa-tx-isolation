package com.redsoft.gda.stat;

import com.redsoft.gda.stat.model.StatData;
import com.redsoft.gda.stat.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Deprecated
@Scope("prototype")
@Component("ReadCommittedFetchDataProcessor")
public class RCFetchDataProcessor implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(RCFetchDataProcessor.class);

    private final WorkSyncConstraints constraints;

    @Autowired
    private StatisticsService service;

    @Autowired
    public RCFetchDataProcessor(WorkSyncConstraints constraints) {
        this.constraints = constraints;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public void run() {
        LOGGER.info("ReadCommitted isolation");
        constraints.lock();
        List<StatData> data = service.findAll();
        constraints.doneFirstFetchCondition();
        LOGGER.info("Fetched data with " + data.size() + " records, wait ...");
        constraints.awaitAddNewRecordCondition();
        data = service.findAll();
        LOGGER.info("Fetched data with " + data.size() + " records, finish");
        constraints.unlock();
    }
}
