package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.banksapi.client.model.LocalDateTimeDeserializer;
import de.banksapi.client.model.incoming.Messages;
import de.banksapi.client.model.incoming.Relation;
import de.banksapi.client.model.incoming.Relations;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * An Issues object contains all information about a banking account that are needed when
 * interacting with the account and serve as the base class for banking accounts ({@link Bankzugang}).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issues implements Relations, Messages, ScaAdvice {

    private String id;
    private UUID providerId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime aktualisierungszeitpunkt;

    private Collection<Message> messages;
    private Collection<TanMedium> tanMedien;
    private Collection<Sicherheitsverfahren> sicherheitsverfahren;
    private Sicherheitsverfahren aktivesSicherheitsverfahren;
    private Challenge challenge;
    private Collection<Relation> relations;

    public String getId() {
        return id;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public LocalDateTime getAktualisierungszeitpunkt() {
        return aktualisierungszeitpunkt;
    }

    @Override
    public Collection<Message> getMessages() {
        return messages;
    }

    @Override
    public Collection<TanMedium> getTanMedien() {
        return tanMedien;
    }

    @Override
    public Collection<Sicherheitsverfahren> getSicherheitsverfahren() {
        return sicherheitsverfahren;
    }

    @Override
    public Sicherheitsverfahren getAktivesSicherheitsverfahren() {
        return aktivesSicherheitsverfahren;
    }

    @Override
    public Challenge getChallenge() {
        return challenge;
    }

    @Override
    public Collection<Relation> getRelations() {
        return relations;
    }

}
