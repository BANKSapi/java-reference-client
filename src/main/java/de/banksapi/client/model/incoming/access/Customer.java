package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.banksapi.client.model.incoming.Messages;
import de.banksapi.client.model.incoming.Relation;
import de.banksapi.client.model.incoming.Relations;

import java.util.Collection;
import java.util.Map;

/**
 * A customer instance serves as a bracket for all Banks/Connect data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Relations, Messages {

    private Map<String, Bankzugang> bankzugaenge;

    private Collection<Relation> relations;
    private Collection<Message> messages;

    public Map<String, Bankzugang> getBankzugaenge() {
        return bankzugaenge;
    }

    @Override
    public Collection<Relation> getRelations() {
        return relations;
    }

    @Override
    public Collection<Message> getMessages() {
        return messages;
    }
}
