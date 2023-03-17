package com.example.APIwiz.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionInfo{
    public String date;
    public Info info;
    public Query query;
    public double result;
    public boolean success;
}

