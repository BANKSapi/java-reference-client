package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.banksapi.client.model.LocalDateTimeDeserializer;
import de.banksapi.client.model.incoming.Relation;
import de.banksapi.client.model.incoming.Relations;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * A Bankprodukt (banking product) is BANKSapi's representation of a bank account, depot or credit
 * card.
 */
//@JsonIgnore
public class Bankprodukt implements Relations {

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
}
