package com.bochkov.duty.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class VacationPK implements Serializable {

    @Column(name = "YEAR")
    Integer year;

    @Column(name = "ID_EMPLOYEE")
    Integer idEmployeer;
}
