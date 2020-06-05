package com.bochkov.duty.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OvertimeData implements Serializable {

    @Column(name = "OVERTIME")
    Duration overTime = Duration.ZERO;

    @Column(name = "WEEKENDTIME")
    Duration weekendTime = Duration.ZERO;

    @Column(name = "RESTTIME")
    Duration restTime = Duration.ZERO;

    @Transient
    public Duration getTotal() {
        return overTime.plus(weekendTime).minus(restTime);
    }
}
