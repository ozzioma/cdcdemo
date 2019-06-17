package com.ozzydemo.cdc.commons.data.domain;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "customertxn")
public class CustomerTransaction extends BaseEntity
{

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @NotNull
    @Column
    private String accountNo;

//    @NotNull
//    @Column
//    private String tranId;

    @NotNull
    @Column
    private String tranRef;

    @NotNull
    @Column
    private LocalDateTime txnDate;

    @NotNull
    @Column
    private String description;


}
