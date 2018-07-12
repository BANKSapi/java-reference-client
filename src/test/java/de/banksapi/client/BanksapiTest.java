package de.banksapi.client;

import de.banksapi.client.services.internal.HttpClient;

import java.util.UUID;

public interface BanksapiTest {

    default void basicResponseCheck(HttpClient.Response response, Integer expectedHttpCode) {
        Integer actualHttpCode = response.getHttpCode();
        String status = actualHttpCode != null ? actualHttpCode.toString() : "null";
        assert response.getError() == null : "An error occurred: " + response.getError() +
                " (HTTP " + status + ")";
        assert expectedHttpCode.equals(actualHttpCode) : "HTTP code " + actualHttpCode + " (actual) " +
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
