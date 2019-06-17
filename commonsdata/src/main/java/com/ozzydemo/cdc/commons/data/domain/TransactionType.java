package com.ozzydemo.cdc.commons.data.domain;

public enum TransactionType
{

    ATM_WITHDRAWAL("ATM_WITHDRAWAL", ""),
    ATM_DEPOSIT("ATM_DEPOSIT", ""),
    TELLER_DEPOSIT("TELLER_DEPOSIT", ""),
    TELLER_WITHDRAWAL("TELLER_WITHDRAWAL", ""),
    WEB_TRANSFER("WEB_TRANSFER", ""),
    MOBILE_TRANSFER("MOBILE_TRANSFER", ""),
    POS_PAYMENT("POS_PAYMENT", ""),
    LOAN_DISBURSE("LOAN_DISBURSE", ""),
    LOAN_REPAYMENT("LOAN_REPAYMENT", ""),
    INTEREST_PAYABLE("INTEREST_PAYABLE", ""),
    INTEREST_RECEIVABLE("INTEREST_RECEIVABLE", ""),
    COT("COT", "");

    private String code;
    private String description;


    TransactionType(String code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public String getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return this.code;
    }
}

