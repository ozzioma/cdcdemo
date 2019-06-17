package com.ozzydemo.cdc.commons.data.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TxnDto
{

    private String accountNo;

    private TransactionType transactionType;

    private String tranId;

    private String tranSeqNo;

    private String tranRef;

    private PostingType type;

    private BigDecimal amount;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime txnDate;

    private String description;
}


