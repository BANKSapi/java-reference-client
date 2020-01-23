package de.banksapi.client;

import de.banksapi.client.model.outgoing.access.LoginCredentials;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;

import java.util.HashMap;
import java.util.UUID;

/**
 * This file holds credentials used to create banking accounts and money transfers in BANKSapi.
 * <p>Most functionality can be accessed using the preset demo account "demo1".</p>
 */
class TestCredentials {

    private static final String USER_ID = "demo1";
    private static final String PIN = "demo";
    private static final String USER_ID_SCA = "test_sca_method_media";
    private static final String PIN_SCA = "test_sca_method_media";

    private static final String ACCOUNT_ID_REST = "d9ecedad-1fcc-43bf-965c-de75a4a76515";
    private static final String ACCOUNT_ID_SYNC_REST = "c5b490c1-1a78-4435-8883-ec1bcc77e8bf";
    private static final String ACCOUNT_ID_HATEOAS = "6aac9219-8271-4a80-ba62-6e202d686bfb";
    private static final String ACCOUNT_ID_SYNC_HATEOAS = "f6168d82-da0e-43d2-8e8a-cad3189d013a";
    private static final String PROVIDER_ID = "00000000-0000-0000-0000-000000000000";

    static LoginCredentialsMap getCredentialsMap(boolean sync, boolean sca, TestSeries testSeries) {
        LoginCredentialsMap loginCredentialsMap = new LoginCredentialsMap();

        HashMap<String, String> loginCredentials = new HashMap<>();
        loginCredentials.put("userid", sca ? USER_ID_SCA : USER_ID);
        loginCredentials.put("pin", sca ? PIN_SCA : PIN);

        String accountId;
        switch (testSeries) {
            case HATEOAS:
                accountId = sync ? ACCOUNT_ID_SYNC_HATEOAS : ACCOUNT_ID_HATEOAS;
                break;
            case REST:
                accountId = sync ? ACCOUNT_ID_SYNC_REST : ACCOUNT_ID_REST;
                break;
            default:
                throw new IllegalArgumentException("TestSeries " + testSeries + " is unknown");
        }

        loginCredentialsMap.put(accountId,
                new LoginCredentials(UUID.fromString(PROVIDER_ID), loginCredentials, sync));

        return loginCredentialsMap;
    }

}
