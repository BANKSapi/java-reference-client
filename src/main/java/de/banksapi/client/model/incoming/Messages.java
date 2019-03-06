package de.banksapi.client.model.incoming;

import de.banksapi.client.model.incoming.access.Message;

import java.util.Collection;

public interface Messages {

    Collection<Message> getMessages();

    default boolean containsMessageCode(String code) {
        return getMessages().stream().anyMatch(m -> m.getCode().equals(code));
    }

}
