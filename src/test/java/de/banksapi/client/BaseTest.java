package de.banksapi.client;

import de.banksapi.client.services.internal.CorrelationIdHolder;
import de.banksapi.client.services.internal.HttpClient.Response;
import junit.framework.AssertionFailedError;

import java.util.UUID;

public interface BaseTest {

    default void basicResponseCheck(Response response, Integer expectedHttpCode) {
        UUID cid = CorrelationIdHolder.get();
        Integer actualHttpCode = response.getHttpCode();
        if (!expectedHttpCode.equals(actualHttpCode)) {
            assertTrue(response.getError() == null, generateErrorMessage(response));
            fail("HTTP code " + actualHttpCode + " (actual) != " + expectedHttpCode + " (expected), " +
                    "CID " + cid);
        }
    }

    default void basicResponseCheckData(Response response, int expectedHttpCode, String subject) {
        basicResponseCheck(response, expectedHttpCode);
        assertTrue(response.getData() != null, "Unable to perform '" + subject + "' request (" +
                generateErrorMessage(response) + ")");
    }

    default String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    default String generateErrorMessage(Response response) {
        UUID cid = CorrelationIdHolder.get();
        return "An error occurred: " + response.getError() + " (HTTP " + response.getHttpCode() + ", " +
                "CID " + cid + ")";
    }

    default void fail(String message) {
        String msg = String.format("%s (CID %s)", message, CorrelationIdHolder.get());
        throw new AssertionFailedError(msg);
    }

    default void assertTrue(Boolean test, String message) {
        if (!test) {
            String msg = String.format("%s (CID %s)", message, CorrelationIdHolder.get());
            throw new AssertionFailedError(msg);
        }
    }

}
