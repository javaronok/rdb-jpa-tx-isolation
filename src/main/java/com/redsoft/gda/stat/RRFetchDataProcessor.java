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
@Component("RepeatableReadFetchDataProcessor")
public class RRFetchDataProcessor implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(RRFetchDataProcessor.class);

    private final WorkSyncConstraints constraints;

    @Autowired
    private StatisticsService service;

    @Autowired
    public RRFetchDataProcessor(WorkSyncConstraints constraints) {
        this.constraints = constraints;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public void run() {
        LOGGER.info("Snapshot isolation");
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
