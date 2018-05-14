package de.banksapi.client.model.incoming.access;

import de.banksapi.client.model.incoming.Relation;
import de.banksapi.client.model.incoming.Relations;

import java.util.Collection;
import java.util.Map;

/**
 * A customer instance serves as a bracket for all Banks/Connect data.
 */
public class Customer implements Relations {

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

    public Collection<Message> getMessages() {
        return messages;
    }
}
