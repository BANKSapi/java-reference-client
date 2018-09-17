package de.banksapi.client.services;

import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.services.internal.CorrelationIdHolder;
import de.banksapi.client.services.internal.HttpClient;

import java.net.URL;
import java.util.UUID;

import static de.banksapi.client.services.internal.HttpClient.PropertyNamingStrategy.LOWER_CAMEL_CASE;

public interface OAuthAwareService {

    OAuth2Token getOAuth2Token();

    default HttpClient createAuthenticatingHttpClient(URL bankzugaengeUrl) {
        HttpClient httpClient = new HttpClient(bankzugaengeUrl, LOWER_CAMEL_CASE)
                .setHeader("Content-type", "application/json")
                .setHeader("Authorization", "bearer " + getOAuth2Token().getAccessToken());

        UUID uuid = CorrelationIdHolder.get();
        if (uuid != null) {
            httpClient.setHeader("X-CORRELATION-ID", uuid.toString());
        }

        return httpClient;
    }

}
