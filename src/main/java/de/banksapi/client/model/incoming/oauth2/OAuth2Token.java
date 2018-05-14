package de.banksapi.client.model.incoming.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuth2Token {

    private String tenant;
    private String client;
    private String user;
    private String scope;
    private UUID accessToken;
    private String tokenType;

    public String getTenant() {
        return tenant;
    }

    public String getClient() {
        return client;
    }

    public String getUser() {
        return user;
    }

    public String getScope() {
        return scope;
    }

    public UUID getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
