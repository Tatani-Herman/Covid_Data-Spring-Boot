package com.project.coviddata.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
/**
 * <p><
 * Classe representant l'entité à persister dans la base de données
 * avec des annotations lombok pour les constructeurs, les getters et les setters
 * /p>
 */
@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
@Table(name="covid_data2")
public class CovidData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name="id")
    private int id;
    @Column(name="Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    @Column(name="pays")
    private String pays;
    @Column(name="Infections")
    private int infections;
    @Column(name="Deces")
    private int deces;
    @Column(name="Guerisons")
    private int guerisons;
    @Column(name="TauxDeces")
    private float tauxdeces;
    @Column(name="TauxGuerison")
    private float tauxguerison;
    @Column(name="TauxInfection")
    private float tauxinfection;

    /**
     * Conctructeur d'un objet CovidData
     * @param date
     *             La date à laquelle les données ont été collectées
     * @param pays
     *             Le pays ou les données ont été collectés
     * @param infections
     *             Le nombre d'infections dans le pays ou les données ont été collectés
     * @param deces
     *             Le nombre de décès dans le pays ou les données ont été collectés
     * @param guerisons
     *             Le nombre de guerisons dans le pays ou les données ont été collectés
     * @param tauxdeces
     *             Le taux de décès dans le pays ou les données ont été collectés
     * @param tauxguerison
     *             Le taux de guerison dans le pays ou les données ont été collectés
     * @param tauxinfection
     *             Le taux d'infection dans le pays ou le données ont été collectés
     */
    public CovidData(LocalDate date, String pays, int infections, int deces, int guerisons, float tauxdeces, float tauxguerison, float tauxinfection) {
        this.date = date;
        this.pays = pays;
        this.infections = infections;
        this.deces = deces;
        this.guerisons = guerisons;
        this.tauxdeces = tauxdeces;
        this.tauxguerison = tauxguerison;
        this.tauxinfection = tauxinfection;
    }

}
