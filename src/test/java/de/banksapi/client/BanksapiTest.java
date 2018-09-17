package de.banksapi.client;

import de.banksapi.client.services.internal.HttpClient.Response;

import java.util.UUID;

import static org.junit.Assert.fail;

public interface BanksapiTest {

    default void basicResponseCheck(Response response, Integer expectedHttpCode, UUID cid) {
        Integer actualHttpCode = response.getHttpCode();
        if (!expectedHttpCode.equals(actualHttpCode)) {
            assert response.getError() == null : generateErrorMessage(response, cid);
            fail("HTTP code " + actualHttpCode + " (actual) != " + expectedHttpCode + " (expected), " +
                    "CID " + cid);
        }
    }

    default void basicResponseCheckData(Response response, int expectedHttpCode, String subject, UUID cid) {
        basicResponseCheck(response, expectedHttpCode, cid);
        assert response.getData() != null : "Unable to perform '" + subject + "' request (" +
                generateErrorMessage(response, cid) + ")";
    }

    default String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    default String generateErrorMessage(Response response, UUID cid) {
        return "An error occurred: " + response.getError() + " (HTTP " + response.getHttpCode() + ", " +
                "CID " + cid + ")";
    }
}
