package de.banksapi.client;

import de.banksapi.client.services.internal.HttpClient.Response;

import java.util.UUID;

import static org.junit.Assert.fail;

public interface BanksapiTest {

    default void basicResponseCheck(Response response, Integer expectedHttpCode) {
        Integer actualHttpCode = response.getHttpCode();
        if (!expectedHttpCode.equals(actualHttpCode)) {
            assert response.getError() == null : generateErrorMessage(response);
            fail("HTTP code " + actualHttpCode + " (actual) != " + expectedHttpCode + " (expected)");
        }
    }

    default void basicResponseCheckData(Response response, int expectedHttpCode, String subject) {
        basicResponseCheck(response, expectedHttpCode);
        assert response.getData() != null : "Unable to perform '" + subject + "' request (" +
                generateErrorMessage(response) + ")";
    }

    default String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    default String generateErrorMessage(Response response) {
        return "An error occurred: " + response.getError() + " (HTTP " + response.getHttpCode() + ")";
    }
}
