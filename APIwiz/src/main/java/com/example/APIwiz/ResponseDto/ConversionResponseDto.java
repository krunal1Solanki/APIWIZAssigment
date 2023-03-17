package com.example.APIwiz.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionResponseDto {
    private String to;
    private String from;
    private double amount;
}
