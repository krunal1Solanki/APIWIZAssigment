package com.example.APIwiz.Controllers;

import com.example.APIwiz.EntryDto.ConversionEntryDto;
import com.example.APIwiz.ResponseDto.ConversionResponseDto;
import com.example.APIwiz.EntryDto.PredictionEntryDto;
import com.example.APIwiz.ResponseDto.PredictionRepsonseDto;
import com.example.APIwiz.Models.Rates;
import com.example.APIwiz.Service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

    @GetMapping("/exchange")
    public ResponseEntity exchange() {
        try {
            return new ResponseEntity<>(currencyService.exchange(), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/convertCurrency")
    public ResponseEntity convertCurrency(@RequestBody ConversionEntryDto conversionEntryDto) {
        try {
            return new ResponseEntity<>(currencyService.convertCurrency(conversionEntryDto), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/predict")
    public ResponseEntity predict(@RequestBody PredictionEntryDto predictionEntryDto) throws Exception{
        try {
            return new ResponseEntity<>(currencyService.predict(predictionEntryDto), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
