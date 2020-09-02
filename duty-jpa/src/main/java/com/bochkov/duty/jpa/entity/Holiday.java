package com.bochkov.duty.jpa.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "HOLIDAY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "title"})
@Accessors(chain = true)
public class Holiday extends AbstractEntity<HolidayPK> {
    @EmbeddedId
    HolidayPK id;

    @Column(name = "title")
    String title;

    @OneToMany(mappedBy = "holiday", orphanRemoval = true)
    List<HolidayDay> holidayDays;


}
