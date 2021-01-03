package com.bochkov.duty.jpa.entity;

import com.bochkov.duty.jpa.entity.converter.DurationConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "EMPLOYEE")
@Entity
public class Employee extends AbstractEntity<Integer> implements Comparable<Employee> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEE_SEQ")
    @SequenceGenerator(name = "EMPLOYEE_SEQ", allocationSize = 1, initialValue = 1000)
    @Column(name = "ID_EMPLOYEE")
    Integer id;

    @Column(unique = true)
    String login;

    @ManyToOne
    @JoinColumn(name = "ID_RANG", nullable = false)
    Rang rang;

    @Column(name = "POST")
    String post;

    @Column(name = "FIRST_NAME")
    String firstName;

    @Column(name = "MIDDLE_NAME")
    String middleName;

    @Column(name = "LAST_NAME")
    String lastName;

    @ManyToOne
    @JoinColumn(name = "ID_EMPLOYEE_GROUP", foreignKey = @ForeignKey(name = "EMPLOYEE_EMPLOYEE_GROUP_FK"))
    EmployeeGroup employeeGroup;

    @JoinTable(name = "EMPLOYEE_SHIFT_TYPE", joinColumns =
    @JoinColumn(name = "ID_EMPLOYEE", referencedColumnName = "ID_EMPLOYEE", foreignKey = @ForeignKey(name = "EMPLOYEE_SHIFT_TYPE_EMPLOYEE_FK")),
            inverseJoinColumns = @JoinColumn(name = "ID_SHIFT_TYPE", referencedColumnName = "ID_SHIFT_TYPE", foreignKey = @ForeignKey(name = "EMPLOYEE_SHIFT_TYPE_SHIFT_TYPE_FK")))
    @ManyToMany
    Set<ShiftType> shiftTypes;

    @Column(name = "ROAD_TO_HOME_TIME")
    @Convert(converter = DurationConverter.class)
    Duration roadToHomeTime;

    @Override
    public String toString() {
        return String.format("%s %s %.1s.%.1s.", Optional.ofNullable(rang).map(Rang::getName).orElse("-"), lastName, firstName, middleName);
    }


    @Override
    public int compareTo(Employee o) {
        return id.compareTo(o.getId());
    }
}
