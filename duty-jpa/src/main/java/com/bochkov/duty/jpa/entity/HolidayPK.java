package com.bochkov.duty.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HolidayPK implements Serializable {
    @Column(name = "year")
    Integer year;
    @Column(name = "id_holiday")
    Integer id;
}
