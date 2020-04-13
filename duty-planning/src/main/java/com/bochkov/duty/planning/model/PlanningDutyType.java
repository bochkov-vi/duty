package com.bochkov.duty.planning.model;

import com.bochkov.duty.jpa.entity.DutyType;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class PlanningDutyType {
    public static final PlanningDutyType EMPTY = new PlanningDutyType(null) {
        @Override
        public int hashCode() {
            return Objects.hashCode(0);
        }
    };

    private DutyType dutyType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanningDutyType)) {
            return false;
        }
        PlanningDutyType that = (PlanningDutyType) o;
        return Objects.equal(dutyType, that.dutyType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dutyType);
    }

    public Integer getId() {
        return Optional.ofNullable(dutyType).map(DutyType::getId).orElse(0);
    }

    public String getName() {
        return Optional.ofNullable(dutyType).map(DutyType::getName).orElse(null);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dutyType", dutyType)
                .toString();
    }
}
