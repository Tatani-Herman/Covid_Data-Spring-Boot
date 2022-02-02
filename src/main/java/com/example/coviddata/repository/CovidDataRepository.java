package com.example.coviddata.repository;

import com.example.coviddata.entities.CovidData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CovidDataRepository extends JpaRepository<CovidData, Integer> {
    List<CovidData> findByPays(String pays);
    List<CovidData> findByPaysAndDate(String country, String date);
}
