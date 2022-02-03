package com.project.coviddata.controller;

import com.project.coviddata.Services.CovidDataService;
import com.project.coviddata.entities.CovidData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CovidDataController {

    @Autowired
    private CovidDataService service;

    //@RequestMapping(path="/deedCovidData")
    @PostMapping("/feedCovidData")
    public ResponseEntity<ResponseMessage> feedData() {
        String message = "";


            try {
                service.saveCoviddata();

                message = "Data saved succesfully ";


                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not save data";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

    @GetMapping("/findAllCovidData")
    public ResponseEntity<List<CovidData>> getAllCovidData() {
        try {
            List<CovidData> data = service.getAllcovidData();

            if (data.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAllCovidDataByCountry/{country}")
    public ResponseEntity<List<CovidData>> getAllCovidDataByCountry(@PathVariable String country) {
        try {
            List<CovidData> data = service.getAllByCountry(country);

            if (data.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAllCovidDataByCountryAndDate/{country}/{date}")
    public ResponseEntity<CovidData> getAllCovidDataByCountry(@PathVariable("country") String country,@PathVariable("date") String date) {
        try {
            CovidData data = service.getAllByCountryAndDate(country, date);

            if (data==null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAllCovidDataByCountryForToday/{country}")
    public ResponseEntity<CovidData> getAllCovidDataByCountryForToday(@PathVariable String country) {
        try {
            CovidData data = service.getAllByCountryAndDateToday(country);

            if (data==null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    }
