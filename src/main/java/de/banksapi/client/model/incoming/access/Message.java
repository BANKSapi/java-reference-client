package de.banksapi.client.model.incoming.access;

/**
 * Messages containing status information about account data retrieval.
 */
public class Message {

    private MessageLevel level;
    private String code;
    private String message;
    private String details;

    public MessageLevel getLevel() {
        return level;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

}
