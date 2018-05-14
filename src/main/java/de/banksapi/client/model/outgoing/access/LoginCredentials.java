package de.banksapi.client.model.outgoing.access;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Login credentials are needed to log in to a banking account. The banking account is identified
 * by a provider ID and the credentials are a map of authentication field keys to authentication
 * data.
 */
public class LoginCredentials {

    private UUID providerId;

    private Map<String, String> credentials = new HashMap<>();

    public LoginCredentials(UUID providerId, Map<String, String> credentials) {
        this.providerId = providerId;
        this.credentials.putAll(credentials);
    }

    public UUID getProviderId() {
        return providerId;
    }

    public Map<String, String> getCredentials() {
        return credentials;
    }

}
