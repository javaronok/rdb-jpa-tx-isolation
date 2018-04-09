package com.redsoft.gda.stat.repository;

import com.redsoft.gda.stat.model.StatData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatDataRepository extends CrudRepository<StatData, Long> {
}
