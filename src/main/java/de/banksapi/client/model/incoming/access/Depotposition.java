package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.banksapi.client.model.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

public class Depotposition {

    private String name;
    private double menge;
    private Handelseinheit handelseinheit;
    private String isin;
    private String wkn;
    private Double kurs;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime kursDatum;

    private String waehrung;
    private Double waehrungskurs;
    private String handelsplatz;
    private Double gesamtwert;

    public String getName() {
        return name;
    }

    public double getMenge() {
        return menge;
    }

    public Handelseinheit getHandelseinheit() {
        return handelseinheit;
    }

    public String getIsin() {
        return isin;
    }

    public String getWkn() {
        return wkn;
    }

    public Double getKurs() {
        return kurs;
    }

    public LocalDateTime getKursDatum() {
        return kursDatum;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public Double getWaehrungskurs() {
        return waehrungskurs;
    }

    public String getHandelsplatz() {
        return handelsplatz;
    }

    public Double getGesamtwert() {
        return gesamtwert;
    }
}
