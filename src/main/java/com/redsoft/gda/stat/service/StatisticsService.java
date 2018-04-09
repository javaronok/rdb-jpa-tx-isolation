package com.redsoft.gda.stat.service;

import com.google.common.collect.Lists;
import com.redsoft.gda.stat.model.StatData;
import com.redsoft.gda.stat.repository.StatDataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final Logger LOGGER = LoggerFactory.getLogger(StatisticsService.class);

    private final StatDataRepository statDataRepository;

    @Transactional(readOnly = true)
    public List<StatData> findAll() {
        return Lists.newArrayList(statDataRepository.findAll());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public List<StatData> findAllWithReadCommitted() {
        return Lists.newArrayList(statDataRepository.findAll());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public List<StatData> findAllWithRepeatableRead() {
        return Lists.newArrayList(statDataRepository.findAll());
    }

    @Transactional
    public void addNewDataRecord(StatData data) {
        statDataRepository.save(data);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public int calcRecordCount(int retryCount, long delay) throws InterruptedException {
        LOGGER.info("Calculation records with " + retryCount + " retries");
        int retry = 0;
        int count = 0;
        while(retry < retryCount) {
            if (retry != 0) {
                Thread.sleep(delay*1000);
            }
            LOGGER.info("Retry " + retry + " ...");
            int received = Lists.newArrayList(statDataRepository.findAll()).size();
            if (count == 0) {
                count = received;
            } else if (count < received) {
                throw new IllegalStateException("Count = " + count + ", received = " + received + " in retry " + retry);
            }
            retry++;
        }
        return count;
    }
}

