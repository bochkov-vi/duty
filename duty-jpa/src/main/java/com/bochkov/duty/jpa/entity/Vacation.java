package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "vacation", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("partNumber")
    Set<VacationPart> parts;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "VACATION_TYPE")
    VacationType type;

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
            for (VacationPart vp : parts.stream().sorted(VacationPart.COMPARATOR).collect(Collectors.toList())) {
                vp.setPartNumber(++i);
            }
        }
    }
}
