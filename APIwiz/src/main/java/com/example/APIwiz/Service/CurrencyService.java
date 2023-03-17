package com.example.APIwiz.Service;

import com.example.APIwiz.EntryDto.ConversionEntryDto;
import com.example.APIwiz.ResponseDto.ConversionResponseDto;
import com.example.APIwiz.EntryDto.PredictionEntryDto;
import com.example.APIwiz.ResponseDto.PredictionRepsonseDto;
import com.example.APIwiz.Models.CurrencyConversionInfo;
import com.example.APIwiz.Models.CurrencyData;
import com.example.APIwiz.Models.Rates;
import com.example.APIwiz.Models.Root;
import com.example.APIwiz.Repository.CurrencyDataRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CurrencyService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CurrencyDataRepository currencyDataRepository;

    public List<Rates> exchange() {
        LocalDate currDate = LocalDate.now().minusDays(1);
        List<Rates> ratesList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

         // reset all the values if present earlier as we want to store only for last 30days
        currencyDataRepository.deleteAll();
        
        int times = 30;

        // looping for 30 api calls decrementing one-one day
        while (times -- > 0) {
            // building url
            String url = "https://api.apilayer.com/fixer/";
            String formattedDate = currDate.format(formatter);
            url += formattedDate + "?symbols=&base=EUR";

            // api call
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", "8nDM3zrg7Lgau3rGu3Oj0VnMpMLlSQNR   ");
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<Root> response = restTemplate.exchange(url, HttpMethod.GET, entity, Root.class);

            // adding in response
            ratesList.add(response.getBody().getRates());

            // storing data into db
            CurrencyData currencyData = new CurrencyData();
            currencyData.setDate(LocalDate.parse(response.getBody().date));
            currencyData.setRate(response.getBody().rates);
            currencyDataRepository.save(currencyData);

            // decrementing date
            currDate = currDate.minusDays(1);
        }

        return ratesList;
    }
    public ConversionResponseDto convertCurrency(ConversionEntryDto conversionEntryDto) throws Exception{

        Set<String> g10CurrencySet = getG10Set();

        if(!g10CurrencySet.contains(conversionEntryDto.getTo()) || !g10CurrencySet.contains(conversionEntryDto.getFrom()))
            throw new Exception("Please enter a G10 currency");

        // url building
        String url = "https://api.apilayer.com/fixer/convert?to=";
        String to = conversionEntryDto.getTo();
        String from = conversionEntryDto.getFrom();
        url += to + "&from=" + from + "&amount=" + conversionEntryDto.getAmount();

        // api call
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", "8nDM3zrg7Lgau3rGu3Oj0VnMpMLlSQNR   ");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<CurrencyConversionInfo> response = restTemplate.exchange(url, HttpMethod.GET, entity, CurrencyConversionInfo.class);


        // object conversion
        ConversionResponseDto conversionResponseDto = new ConversionResponseDto();
        conversionResponseDto.setTo(response.getBody().query.getMyto());
        conversionResponseDto.setFrom(response.getBody().query.getFrom());
        conversionResponseDto.setAmount(response.getBody().result);

        return conversionResponseDto;
    }

    private Set<String> getG10Set() {
        Set<String> g10Currencies = new HashSet<String>();
        g10Currencies.add("USD");
        g10Currencies.add("EUR");
        g10Currencies.add("JPY");
        g10Currencies.add("GBP");
        g10Currencies.add("CHF");
        g10Currencies.add("CAD");
        g10Currencies.add("AUD");
        g10Currencies.add("NZD");
        g10Currencies.add("NOK");
        g10Currencies.add("SEK");
        return g10Currencies;
    }

    public PredictionRepsonseDto predict(PredictionEntryDto predictionEntryDto) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<CurrencyData> currencyDataList = currencyDataRepository.findAll();
        String currencyCode = predictionEntryDto.getCurrency();

        // building function name to get invoke
        String fun = "get";
        fun += currencyCode.substring(0,1).toLowerCase() + currencyCode.substring(1);

        double sum = 0;
        PredictionRepsonseDto dto = new PredictionRepsonseDto();

        for(CurrencyData currencyData : currencyDataList) {
            // mapping string to function name
            Rates rates = currencyData.getRate();
            java.lang.reflect.Method method = rates.getClass().getMethod(fun);
            sum += (double) method.invoke(rates);

            // if the date equals to the known date return its values
            if(currencyData.getDate().equals(predictionEntryDto.getDate())) {
                dto.setPredictedValue((double) method.invoke(rates));
                return dto;
            }
        }

        // return the average of all dates
        dto.setPredictedValue(sum/currencyDataList.size());
        return dto;
    }
}
