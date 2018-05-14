package de.banksapi.client;

import de.banksapi.client.services.internal.HttpClient;

import java.util.UUID;

public interface BanksapiTest {

    default void basicResponseCheck(HttpClient.Response response, int expectedHttpCode) {
        assert response.getError() == null : "An error occurred: " + response.getError();
        int actualHttpCode = response.getHttpCode();
        assert actualHttpCode == expectedHttpCode : "HTTP code " + actualHttpCode + " (actual) " +
                "!= " + expectedHttpCode + " (expected)";
    }

    default void basicResponseCheckData(HttpClient.Response response, int expectedHttpCode, String subject) {
        basicResponseCheck(response, expectedHttpCode);
        assert response.getData() != null : "Unable to perform '" + subject + "' request";
    }

    default String generateRandomString() {
        return UUID.randomUUID().toString();
    }

}
