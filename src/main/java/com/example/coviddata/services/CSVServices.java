package com.example.coviddata.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.coviddata.entities.CovidData;
import com.example.coviddata.repository.CovidDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CSVServices {
    @Autowired
    CovidDataRepository repository;

    public void save(MultipartFile file) {
        try {
            List<CovidData> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
            repository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<CovidData> tutorials = repository.findAll();

        ByteArrayInputStream in = CSVHelper.tutorialsToCSV(tutorials);
        return in;
    }

    public List<CovidData> getAllcovidData() {
        return repository.findAll();
    }

    public List<CovidData> getAllByCountry(String pays){
        return repository.findByPays(pays);
    }

    public List<CovidData> getAllByCountryAndDate(String country, String date){
        return repository.findByPaysAndDate(country,date);
    }

    public List<CovidData> getAllByCountryAndDateToday(String country){
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String todayAsString = df.format(today);
        return repository.findByPaysAndDate(country,todayAsString);
    }
}