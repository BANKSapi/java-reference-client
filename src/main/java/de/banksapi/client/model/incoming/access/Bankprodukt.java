package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.banksapi.client.model.LocalDateTimeDeserializer;
import de.banksapi.client.model.incoming.Messages;
import de.banksapi.client.model.incoming.Relation;
import de.banksapi.client.model.incoming.Relations;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * A Bankprodukt (banking product) is BANKSapi's representation of a bank account, depot or credit
 * card.
 */
public class Bankprodukt implements Relations, Messages {

    private String id;
    private String bezeichnung;
    private Kategorie kategorie;
    private Double saldo;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime aktualisierungszeitpunkt;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime saldoDatum;

    private String waehrung;
    private String kontonummer;
    private String iban;
    private String bic;
    private String blz;
    private String kreditinstitut;
    private String inhaber;

    private Collection<Relation> relations;
    private Collection<Message> messages;

    // only relevant for depots
    private String saldoDatenquelle;

    // only relevant for banking accounts
    private Double ueberziehungslimit;
    private Double verfuegungsrahmen;
    private Double verfuegterBetrag;

    // only relevant for building savings (Bausparen)
    private String vertragsnummer;
    private Double rating;
    private Double vertragssumme;
    private String vertragstyp;
    private Boolean sparzustand;
    private String vertragsstatus;
    private Double sparzinssatz;
    private Double schuldzinssatz;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime vertragsDatum;
    private Double sparfortschritt;

    public String getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public Double getSaldo() {
        return saldo;
    }

    public LocalDateTime getAktualisierungszeitpunkt() {
        return aktualisierungszeitpunkt;
    }

    public LocalDateTime getSaldoDatum() {
        return saldoDatum;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public String getBlz() {
        return blz;
    }

    public String getKreditinstitut() {
        return kreditinstitut;
    }

    public String getInhaber() {
        return inhaber;
    }

    @Override
    public Collection<Relation> getRelations() {
        return relations;
    }

    @Override
    public Collection<Message> getMessages() {
        return messages;
    }

    public String getSaldoDatenquelle() {
        return saldoDatenquelle;
    }

    public Double getUeberziehungslimit() {
        return ueberziehungslimit;
    }

    public Double getVerfuegungsrahmen() {
        return verfuegungsrahmen;
    }

    public Double getVerfuegterBetrag() {
        return verfuegterBetrag;
    }

    public String getVertragsnummer() {
        return vertragsnummer;
    }

    public Double getRating() {
        return rating;
    }

    public Double getVertragssumme() {
        return vertragssumme;
    }

    public String getVertragstyp() {
        return vertragstyp;
    }

    public Boolean getSparzustand() {
        return sparzustand;
    }

    public String getVertragsstatus() {
        return vertragsstatus;
    }

    public Double getSparzinssatz() {
        return sparzinssatz;
    }

    public Double getSchuldzinssatz() {
        return schuldzinssatz;
    }

    public LocalDateTime getVertragsDatum() {
        return vertragsDatum;
    }

    public Double getSparfortschritt() {
        return sparfortschritt;
    }
}
