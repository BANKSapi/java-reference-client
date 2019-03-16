package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.banksapi.client.model.LocalDateTimeDeserializer;
import de.banksapi.client.model.incoming.Messages;
import de.banksapi.client.model.incoming.Relation;
import de.banksapi.client.model.incoming.Relations;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * A Bankzugang (banking account) contains all linked banking products {@link Bankprodukt} and
 * informationen about the account's second factor authentication configuration used for
 * transactions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bankzugang implements Relations, Messages {

    private BankzugangStatus status;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime aktualisierungszeitpunkt;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeout;

    private Collection<TanMedium> tanMedien;
    private Collection<Sicherheitsverfahren> sicherheitsverfahren;
    private Sicherheitsverfahren aktivesSicherheitsverfahren;

    private Collection<Bankprodukt> bankprodukte;

    private Collection<Relation> relations;
    private Collection<Message> messages;

    private boolean sync;

    public BankzugangStatus getStatus() {
        return status;
    }

    public LocalDateTime getAktualisierungszeitpunkt() {
        return aktualisierungszeitpunkt;
    }

    public LocalDateTime getTimeout() {
        return timeout;
    }

    public Collection<TanMedium> getTanMedien() {
        return tanMedien;
    }

    public Collection<Sicherheitsverfahren> getSicherheitsverfahren() {
        return sicherheitsverfahren;
    }

    public Sicherheitsverfahren getAktivesSicherheitsverfahren() {
        return aktivesSicherheitsverfahren;
    }

    public Collection<Bankprodukt> getBankprodukte() {
        return bankprodukte;
    }

    @Override
    public Collection<Relation> getRelations() {
        return relations;
    }

    @Override
    public Collection<Message> getMessages() {
        return messages;
    }

    public boolean isSync() {
        return sync;
    }
}
