package com.project.coviddata.repositories;

import com.project.coviddata.entities.CovidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Interface recensant les méthodes permettant d'interragir avec
 * la base de données
 * </p>
 */

@Repository
public interface CovidDataRepository extends JpaRepository<CovidData,Integer> {
    /**
     * @param country
     * @return la liste des données sur le covid pour un pays donné
     */
    List<CovidData> findByPays(String country);

    /**
     * @param country
     * @param date
     * @return la lsite données sur le covid pour un pays donné et pour une date précise
     */
    List<CovidData> findByPaysAndDate(String country, LocalDate date);
}
