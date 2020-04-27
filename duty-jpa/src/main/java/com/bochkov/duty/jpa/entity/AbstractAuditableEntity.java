package com.bochkov.duty.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
public abstract class AbstractAuditableEntity<ID extends Serializable> extends AbstractEntity<ID> {
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    String lastModifiedBy;

    @CreatedBy
    @Column(name = "CREATED_BY")
    String createdBy;
}
