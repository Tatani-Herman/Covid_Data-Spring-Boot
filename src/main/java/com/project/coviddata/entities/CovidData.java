package com.project.coviddata.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name="covid_data2")
public class CovidData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name="id")
    private int id;
    @Column(name="Date")
    private String date;
    @Column(name="pays")
    private String pays;
    @Column(name="Infections")
    private int infections;
    @Column(name="Deces")
    private int deces;
    @Column(name="Guerisons")
    private int guerisons;

    public CovidData(String date, String pays, int infections, int deces, int guerisons, float tauxdeces, float tauxguerison, float tauxinfection) {
        this.date = date;
        this.pays = pays;
        this.infections = infections;
        this.deces = deces;
        this.guerisons = guerisons;
        this.tauxdeces = tauxdeces;
        this.tauxguerison = tauxguerison;
        this.tauxinfection = tauxinfection;
    }

    @Column(name="TauxDeces")
    private float tauxdeces;
    @Column(name="TauxGuerison")
    private float tauxguerison;
    @Column(name="TauxInfection")
    private float tauxinfection;


}
