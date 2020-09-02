package com.bochkov.duty.jpa.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "HOLIDAY_DAY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "type"})
@Accessors(chain = true)
public class HolidayDay extends AbstractEntity<LocalDate> {
    @Id
    @Column(name = "date")
    LocalDate id;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "year", referencedColumnName = "year"), @JoinColumn(name = "id_holiday", referencedColumnName = "id_holiday")})
    Holiday holiday;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "holiday_type", nullable = false)
    HolidayType type;

    public boolean isShortened() {
        return type == HolidayType.SHORTENED;
    }

    public boolean isWeekend() {
        return type == HolidayType.WEEKEND;
    }

    public boolean isWorkday() {
        return type == HolidayType.WEEKEND;
    }

    public enum HolidayType {
        SHORTENED, WEEKEND, WORKED
    }
}
