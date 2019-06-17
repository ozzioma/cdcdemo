package com.ozzydemo.cdc.commons.data.domain;

public enum PostingType
{
    DEBIT("DEBIT", ""),
    CREDIT("CREDIT", "");

    private String code;
    private String description;


    PostingType(String code, String description)
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
