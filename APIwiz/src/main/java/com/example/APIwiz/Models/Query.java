package com.example.APIwiz.Models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query {
    public int amount;
    public String from;
    @JsonProperty("to")
    public String myto;
}
