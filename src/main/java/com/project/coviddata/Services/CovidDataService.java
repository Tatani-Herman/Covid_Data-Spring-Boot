package com.project.coviddata.Services;

import com.project.coviddata.entities.CovidData;
import com.project.coviddata.repositories.CovidDataRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CovidDataService {

    @Autowired
    private CovidDataRepository repository;

    private static final long ONE_DAY_MILLI_SECONDS = 24 * 60 * 60 * 1000;

    String line ="";

    RestTemplate rt = new RestTemplate();
    String url = "https://coronavirus.politologue.com/data/coronavirus/coronacsv.aspx?format=csv&t=pays";

    @PostConstruct
    @Scheduled(fixedRate=10800000)
    public void saveCoviddata() throws IOException {
        // get covid data
        ResponseEntity<Resource> response = rt.getForEntity(url, Resource.class);

        //write covid data to csv file
        File target = new File("covid-data.csv");
        InputStream is = Objects.requireNonNull(response.getBody()).getInputStream();
        FileUtils.copyInputStreamToFile(is,target);

        // save data to database
        try {
            BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));

            for(int i=1; i<=8;i++) {
                br.readLine(); // this will read the first 8 lines to remove the header from data
            }
            while((line=br.readLine())!=null){
                String [] data = line.split(";");
                CovidData cd = new CovidData();
                cd.setDate(data[0]);
                cd.setPays(data[1]);
                cd.setInfections(Integer.parseInt(data[2]));
                cd.setDeces(Integer.parseInt(data[3]));
                cd.setGuerisons(Integer.parseInt(data[4]));
                cd.setTauxdeces(Float.parseFloat(data[5]));
                cd.setTauxguerison(Float.parseFloat(data[6]));
                cd.setTauxinfection(Float.parseFloat(data[7]));

                repository.save(cd);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public void saveCoviddata(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));
            br.readLine(); // this will read the first line to remove the header from data
            while((line=br.readLine())!=null){
                String [] data = line.split(",");
                CovidData cd = new CovidData();
                cd.setDate(data[0]);
                cd.setPays(data[1]);
                cd.setInfections(Integer.parseInt(data[2]));
                cd.setDeces(Integer.parseInt(data[3]));
                cd.setGuerisons(Integer.parseInt(data[4]));
                cd.setTauxdeces(Float.parseFloat(data[5]));
                cd.setTauxguerison(Float.parseFloat(data[6]));
                cd.setTauxinfection(Float.parseFloat(data[7]));

                repository.save(cd);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public List<CovidData> getAllcovidData() {
        return repository.findAll();
    }

    public List<CovidData> getAllByCountry(String pays){
        return repository.findByPays(pays);
    }

    public CovidData getAllByCountryAndDate(String country, String date) throws ParseException {

        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        Date date1= df.parse(date);

        long previousDayMilliSeconds = date1.getTime() - ONE_DAY_MILLI_SECONDS;
        Date previousDate = new Date(previousDayMilliSeconds);
        String previousDateStr = df.format(previousDate);
       CovidData cd = repository.findByPaysAndDate(country,previousDateStr);
       CovidData cd2 = repository.findByPaysAndDate(country, date);
        CovidData cd3 =  new CovidData();
        cd3.setDate(date);
        cd3.setPays(country);
        cd3.setInfections(cd2.getInfections()-cd.getInfections());
        cd3.setDeces(cd2.getDeces()-cd.getDeces());
        cd3.setGuerisons(cd2.getGuerisons()-cd.getGuerisons());
        cd3.setTauxinfection(cd2.getTauxinfection());
        cd3.setTauxguerison(cd2.getTauxguerison());
        cd3.setTauxdeces(cd2.getTauxdeces());

        return cd3;
    }

    public CovidData getAllByCountryAndDateToday(String country){
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String todayAsString = df.format(today);
        return repository.findByPaysAndDate(country,todayAsString);
    }
}
