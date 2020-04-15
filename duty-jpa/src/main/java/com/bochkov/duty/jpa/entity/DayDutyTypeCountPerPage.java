package com.bochkov.duty.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
@EqualsAndHashCode(of = {"dutyType"})
public class DayDutyTypeCountPerPage implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_duty_type", referencedColumnName = "id_duty_type")
    DutyType dutyType;

    @Column(name = "max_count_per_day")
    Integer maxCountPerDay = 1;

    @Column(name = "min_count_per_day")
    Integer minCountPerDay = 1;
}
