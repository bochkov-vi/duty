package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "VACATION")
@Entity
@NoArgsConstructor
public class Vacation extends AbstractEntity<VacationPK> {

    @EmbeddedId
    VacationPK id;

    @ManyToOne
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE", insertable = false, updatable = false)
    Employee employee;

    @ElementCollection
    @CollectionTable(name = "VACATION_PART", joinColumns = {@JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE"), @JoinColumn(name = "YEAR", referencedColumnName = "YEAR")})
    @OrderBy("partNumber")
    Set<VacationPart> parts;

    public Vacation(VacationPK id) {
        this.id = id;
    }

    public static Vacation of(Integer year, Employee employee) {
        return new Vacation().setEmployee(employee).setId(new VacationPK(year, employee.getId()));
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if (parts != null) {
            int i = 0;
            for (VacationPart vp : parts) {
                vp.setPartNumber(++i);
            }
        }
    }
}
