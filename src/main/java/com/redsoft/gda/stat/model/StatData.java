package com.redsoft.gda.stat.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "STAT_DATA")
public class StatData {

    @Id
    @SequenceGenerator(name = "stat_data_gen", sequenceName = "STAT_DATA_SEQ", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="stat_data_gen")
    private Long id;

    @Column(length = 18, precision = 2, columnDefinition = "DECIMAL(18,2)")
    private BigDecimal charge;

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }
}
