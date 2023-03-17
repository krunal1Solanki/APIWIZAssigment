package com.example.APIwiz.Models;

import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Root{
    public String base;
    public String date;
    public boolean historical;
    public Rates rates;
    public boolean success;
    public int timestamp;
}

