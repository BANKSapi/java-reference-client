package de.banksapi.client.services.internal;

class JsonSerializationException extends IllegalStateException {

    JsonSerializationException(String msg, Exception cause) {
        super(msg, cause);
    }

}
