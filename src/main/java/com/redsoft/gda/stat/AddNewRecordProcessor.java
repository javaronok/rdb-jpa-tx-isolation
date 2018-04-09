package com.redsoft.gda.stat;

import com.redsoft.gda.stat.model.StatData;
import com.redsoft.gda.stat.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Scope("prototype")
@Component("AddNewRecordProcessor")
public class AddNewRecordProcessor implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(AddNewRecordProcessor.class);

    private final WorkSyncConstraints constraints;

    @Autowired
    private StatisticsService service;

    @Autowired
    public AddNewRecordProcessor(WorkSyncConstraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public void run() {
        constraints.lock();
        StatData data = new StatData();
        data.setCharge(new BigDecimal(1000.00));
        LOGGER.info("Data prepared wait ...");
        constraints.awaitFirstFetchCondition();
        service.addNewDataRecord(data);
        LOGGER.info("Inserted record");
        constraints.doneAddNewRecordCondition();
        constraints.unlock();
    }
}
