package de.banksapi.client;

import de.banksapi.client.services.internal.HttpClient;
import java.net.URL;
import java.util.UUID;

public abstract class BanksapiTest {
    
    static BANKSapiConfig defaultTestConfig() {
        return new BANKSapiConfig(BANKSapi.getBanksapiBase());
    }

    void basicResponseCheck(HttpClient.Response response, int expectedHttpCode) {
        assert response.getError() == null : "An error occurred: " + response.getError();
        int actualHttpCode = response.getHttpCode();
        assert actualHttpCode == expectedHttpCode : "HTTP code " + actualHttpCode + " (actual) " +
                "!= " + expectedHttpCode + " (expected)";
    }

    void basicResponseCheckData(HttpClient.Response response, int expectedHttpCode, String subject) {
        basicResponseCheck(response, expectedHttpCode);
        assert response.getData() != null : "Unable to perform '" + subject + "' request";
    }

    String generateRandomString() {
        return UUID.randomUUID().toString();
    }

}
