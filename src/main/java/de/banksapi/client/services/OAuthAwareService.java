package de.banksapi.client.services;

import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.services.internal.HttpClient;

import java.net.URL;

import static de.banksapi.client.services.internal.HttpClient.PropertyNamingStrategy.LOWER_CAMEL_CASE;

public interface OAuthAwareService {

    OAuth2Token getOAuth2Token();

    default HttpClient createAuthenticatingHttpClient(URL bankzugaengeUrl) {
        return new HttpClient(bankzugaengeUrl, LOWER_CAMEL_CASE)
                .setHeader("Content-type", "application/json")
                .setHeader("Authorization", "bearer " + getOAuth2Token().getAccessToken());
    }

}
