package com.project.coviddata.repositories;

import com.project.coviddata.entities.CovidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CovidDataRepository extends JpaRepository<CovidData,Integer> {

    List<CovidData> findByPays(String country);
    //List<CovidData> findByPaysAndDate(String country, String date);
    CovidData findByPaysAndDate(String country, String date);
}
