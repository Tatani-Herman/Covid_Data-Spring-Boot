package com.project.coviddata;

import com.project.coviddata.Services.CovidDataService;
import com.project.coviddata.controller.CovidDataController;
import com.project.coviddata.entities.CovidData;
import com.project.coviddata.repositories.CovidDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@SpringBootTest
class CovidDataApplicationTests {

    @Autowired
    private CovidDataRepository repository;

    @Autowired
    private CovidDataService service;

    @Autowired
    private CovidDataController controller;


    @Test
    public void testCreateCovidData(){
        CovidData cd = new CovidData(LocalDate.now(),"France",1,1,1,0,0,0);
        repository.save(cd);
    }

    @Test
    public void testFindCovidData(){
        CovidData cd = repository.findById(1).orElse(null);
        System.out.println(cd);
    }

    @Test
    public void testUpdateCovidData() {
        CovidData cd = repository.findById(1).orElse(null);
        if(cd!=null){
        cd.setPays("Zambie");
        repository.save(cd);}
    }

   @Test
    public void testDeleteCovidData(){
        CovidData cd = repository.findById(1).orElse(null);
        if(cd!=null)
        repository.deleteById(1);
    }

    @Test
    public void testFindAllCovidData(){
        List<CovidData> cds = repository.findAll();

        for(CovidData cd : cds)
            System.out.println(cd);
    }

    @Test
    public void testGetAllByCountry(){
        String pays="Cameroun";
        List<CovidData> cds = service.getAllByCountry(pays);

        for(CovidData cd : cds)
            System.out.println(cd);
    }

    @Test
    public void testGetAllByCountryAndDate() throws ParseException {
        String pays="France";
        String date="2022-01-31";

        CovidData cd = service.getAllByCountryAndDate(pays,date);
        System.out.println(cd);
    }

    @Test
    public void testGetAllByCountryAndDateToday() throws ParseException {
        String pays="France";
        CovidData cd = service.getAllByCountryAndDateToday(pays);
        System.out.println(cd);
    }


    @Test
    void contextLoads() {
    }

}
