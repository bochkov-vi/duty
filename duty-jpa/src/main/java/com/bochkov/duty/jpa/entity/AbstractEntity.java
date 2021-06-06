package com.bochkov.duty.jpa.entity;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderColumn;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity<ID extends Serializable> implements Serializable, Persistable<ID> {

    @OrderColumn()
    @CreatedDate
    @Column(name = "CREATED_DATE")
    LocalDateTime createdDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEntity)) {
            return false;
        }
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return Objects.equal(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
