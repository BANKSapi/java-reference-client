package de.banksapi.client;

import de.banksapi.client.services.internal.HttpClient;

import java.util.UUID;

import static org.junit.Assert.fail;

public interface BanksapiTest {

    default void basicResponseCheck(HttpClient.Response response, Integer expectedHttpCode) {
        Integer actualHttpCode = response.getHttpCode();
        String status = actualHttpCode != null ? actualHttpCode.toString() : "null";
        if (!expectedHttpCode.equals(actualHttpCode)) {
            assert response.getError() == null : "An error occurred: " + response.getError() +
                    " (HTTP " + status + ")";
            fail("HTTP code " + actualHttpCode + " (actual) != " + expectedHttpCode + " (expected)");
        }
    }

    default void basicResponseCheckData(HttpClient.Response response, int expectedHttpCode, String subject) {
        basicResponseCheck(response, expectedHttpCode);
        assert response.getData() != null : "Unable to perform '" + subject + "' request";
    }

    default String generateRandomString() {
        return UUID.randomUUID().toString();
    }

}
