package com.ozzydemo.cdc.commons.data.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "txnmaster")
public class TransactionMaster extends BaseEntity
{

    @NotNull
    @Column
    private String tranId;

    @NotNull
    @Column
    private String tranSeqNo;

    @NotNull
    @Column
    private String tranRef;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private PostingType type;

    @NotNull
    @Column
    private LocalDateTime datePosted;

    @NotNull
    @Column
    private BigDecimal amount;

//    @NotNull
//    @Column
//    private BigDecimal credit;

    @NotNull
    @Column
    private String description;
}
