package com.project.coviddata.Services;

import com.project.coviddata.controller.CovidDataController;
import com.project.coviddata.entities.CovidData;
import com.project.coviddata.repositories.CovidDataRepository;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>Classe permettant d'implementer tous les services
 * de notre application</p>
 */
@Service
public class CovidDataService {

    @Autowired
    private CovidDataRepository repository; //Activation de l'injection automatique de dépendance
    private static final Logger logger = LogManager.getLogger(CovidDataService.class);

    RestTemplate rt = new RestTemplate();
    private static final String url = "https://coronavirus.politologue.com/data/coronavirus/coronacsv.aspx?format=csv&t=pays";

    /**
     * <p>méthode permettant de télécharger le fichier CSV depuis l'url
     * et d'enregistrer les données du fichier dans la base de données MySQL</p>
     * @throws IOException
     */
  @PostConstruct
    @Scheduled(fixedRate=10800001)
    public void saveCoviddata() throws IOException {
      String line ="";

        // Suppresion de toutes les données de la base de données avant l'ajout de nouvelles
      repository.deleteAll();

        // Récupération des données sur le covid depuis l'url
        ResponseEntity<Resource> response = rt.getForEntity(url, Resource.class);

        // Ecriture des données récupéré dans un fichier CSV
        File target = new File("covid-data.csv");
        InputStream is = Objects.requireNonNull(response.getBody()).getInputStream();
        FileUtils.copyInputStreamToFile(is,target);

        // Enregistrement des données dans la base de données
        try {
            BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));

            // Suppression des 8 premières lignes du fichiers CSV qui représentent l'en-tete
            for(int i=1; i<=8;i++) {
                br.readLine();
            }
           logger.info("en-tete enlevé");

            while((line=br.readLine())!=null){
                String [] data = line.split(";");
                CovidData cd = new CovidData();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                cd.setDate(LocalDate.parse(data[0], formatter));
                cd.setPays(data[1]);
                cd.setInfections(Integer.parseInt(data[2]));
                cd.setDeces(Integer.parseInt(data[3]));
                cd.setGuerisons(Integer.parseInt(data[4]));
                cd.setTauxdeces(Float.parseFloat(data[5]));
                cd.setTauxguerison(Float.parseFloat(data[6]));
                cd.setTauxinfection(Float.parseFloat(data[7]));

                repository.save(cd);
                logger.info("donnée insérée");
            }
        } catch (IOException e) {
            logger.error("les données n'ont pas été inséré dans la base de données");
            e.printStackTrace();
        }
    }

    /**
     * @return la liste des données sur le covid
     */
    public List<CovidData> getAllcovidData() {
        logger.info("recherche de tous les données sur le covid");
        return repository.findAll();
    }

    /**
     * @param pays
     * @return la liste des données sur le covid pour un pays précis
     */
    public List<CovidData> getAllByCountry(String pays){
        logger.info("recherche des données par pays");
        return repository.findByPays(pays);
    }

    /**
     *
     * @param country
     * @param date
     * @return la liste des données sur le covid pour un pays donné et pour une date précise
     * @throws ParseException
     */
    public CovidData getAllByCountryAndDate(String country, String date) throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date1 = LocalDate.parse(date, formatter);
        LocalDate previousDate = date1.minusDays(1);
        List<CovidData> cd = repository.findByPaysAndDate(country,previousDate);
        List<CovidData>cd2 = repository.findByPaysAndDate(country, date1);
        CovidData cd3 =  new CovidData();
        if(cd2!=null && cd2.get(0).getInfections()!=cd.get(0).getInfections()) {
            logger.info("données du jour trouvées");
            cd3.setDate(date1);
            cd3.setPays(country);
            cd3.setInfections(cd2.get(0).getInfections() - cd.get(0).getInfections());
            cd3.setDeces(cd2.get(0).getDeces() - cd.get(0).getDeces());
            cd3.setGuerisons(cd2.get(0).getGuerisons() - cd.get(0).getGuerisons());
            cd3.setTauxinfection(cd2.get(0).getTauxinfection());
            cd3.setTauxguerison(cd2.get(0).getTauxguerison());
            cd3.setTauxdeces(cd2.get(0).getTauxdeces());
        }
        else {
             logger.warn("données du jour pas trouvées");
            cd3=null;
        }

        return cd3;
    }

    /**
     *
     * @param country
     * @return les informations sur le covid pour un pays donné et pour la date du jour
     * @throws ParseException
     */
    public  CovidData getAllByCountryAndDateToday(String country) throws ParseException {
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String toaystr = df.format(today);
         logger.info("recherche des données pour un pays et correspondant a la date du jour");
        return this.getAllByCountryAndDate(country,toaystr);
    }
}
