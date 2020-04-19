package com.bochkov.duty.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OvertimeData implements Serializable {
    Duration overTime = Duration.ZERO;
    Duration weekendTime = Duration.ZERO;
    Duration restTime = Duration.ZERO;
}
