package com.example.APIwiz.EntryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionEntryDto {
    private String to;
    private String from;
    private int amount;
}
