package com.example.APIwiz.Repository;

import com.example.APIwiz.Models.CurrencyData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CurrencyDataRepository extends MongoRepository<CurrencyData, LocalDate > {
}
