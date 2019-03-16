package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.banksapi.client.model.LocalDateTimeDeserializer;
import de.banksapi.client.model.incoming.Relation;
import de.banksapi.client.model.incoming.Relations;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * A Kontoumsatz (account turnover) contains all information about account movements.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Kontoumsatz implements Relations {

    private UUID id;
    private double betrag;
    private String verwendungszweck;
    private String buchungstext;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime buchungsdatum;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime wertstellungsdatum;

    private String gegenkontoInhaber;
    private String gegenkontoIban;
    private String gegenkontoBic;
    private String gvCode;
    private String primanotaNummer;

    private String hash;

    private Collection<Relation> relations;

    public UUID getId() {
        return id;
    }

    public double getBetrag() {
        return betrag;
    }

    public String getVerwendungszweck() {
        return verwendungszweck;
    }

    public String getBuchungstext() {
        return buchungstext;
    }

    public LocalDateTime getBuchungsdatum() {
        return buchungsdatum;
    }

    public LocalDateTime getWertstellungsdatum() {
        return wertstellungsdatum;
    }

    public String getGegenkontoInhaber() {
        return gegenkontoInhaber;
    }

    public String getGegenkontoIban() {
        return gegenkontoIban;
    }

    public String getGegenkontoBic() {
        return gegenkontoBic;
    }

    public String getGvCode() {
        return gvCode;
    }

    public String getPrimanotaNummer() {
        return primanotaNummer;
    }

    public String getHash() {
        return hash;
    }

    public Collection<Relation> getRelations() {
        return relations;
    }

}
