package com.ozzydemo.cdc.commons.data.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
public class BaseEntity
{

    @Id
    @GeneratedValue(generator = "seq_txnbase", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_txnbase", allocationSize = 1, sequenceName = "seq_txnbase")
    @Column(name = "id", updatable = false, nullable = false)
    protected Long id;
}