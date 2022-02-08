package com.project.coviddata.controller;

import com.project.coviddata.Services.CovidDataService;
import com.project.coviddata.entities.CovidData;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CovidDataController {

    @Autowired
    private CovidDataService service; //Activation de l'injection automatique de dépendance
    private static final Logger logger = LogManager.getLogger(CovidDataController.class);

    /**
     * @return la liste de tous les données sur le covid
     */
    @ApiOperation(value ="permet de visualiser toutes les informations sur les cas de covid par jour et par pays")
    @GetMapping("/findAllCovidData")
    public ResponseEntity<List<CovidData>> getAllCovidData() {
        try {
            List<CovidData> data = service.getAllcovidData();


            if (data.isEmpty()) {
                logger.warn("Pas de données trouvées");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            logger.info("Données trouvées");
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Problème survenu durant la recherche de données"+e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * @return la liste de tous les données sur le covid pour un pays précis
     */
    @ApiOperation(value ="permet de visualiser toutes les informations sur les cas de covid pour un pays donné")
    @GetMapping("/findAllCovidDataByCountry/{country}")
    public ResponseEntity<List<CovidData>> getAllCovidDataByCountry(final @PathVariable String country) {
        try {
            List<CovidData> data = service.getAllByCountry(country);

            if (data.isEmpty()) {
                   logger.warn("Pas de données trouvées");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
             logger.info("Données trouvées");
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Problème survenu durant la recherche de données"+e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @return les données sur le covid pour un pays donné et une date précise
     */
    @ApiOperation(value ="permet de visualiser toutes les informations sur les cas de covid pour un pays donné et pour une date précise")
    @GetMapping("/findAllCovidDataByCountryAndDate/{country}/{date}")
    public ResponseEntity<CovidData> getAllCovidDataByCountry(final @PathVariable("country") String country,final @PathVariable("date") String date) throws ParseException {
        try {
            CovidData data = service.getAllByCountryAndDate(country, date);

            if (data==null) {
                logger.warn("Pas de données trouvées");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
             logger.info("Données trouvées");
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Problème survenu durant la recherche de données"+e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @return les données sur le covid pour un pays donné et pour la date du jour
     */
    @ApiOperation(value ="permet de visualiser toutes les informations sur les cas de covid pour un pays donné et pour la date du jour")
    @GetMapping("/findAllCovidDataByCountryForToday/{country}")
    public ResponseEntity<CovidData> getAllCovidDataByCountryForToday(final @PathVariable String country) throws ParseException {
        try {
            CovidData data = service.getAllByCountryAndDateToday(country);

            if (data==null) {
                logger.warn("Pas de données trouvées");
                return new ResponseEntity("Les données n'ont pas été mise à jour", HttpStatus.OK);
            }
            logger.info("données trouvées");
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Problème survenu durant la recherche de données"+e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
