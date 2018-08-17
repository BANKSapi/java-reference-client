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
    private static final String ACCOUNT_ID = "demo";
    private static final String ACCOUNT_ID_SYNC = "c5b490c1-1a78-4435-8883-ec1bcc77e8bf";
    private static final String PROVIDER_ID = "00000000-0000-0000-0000-000000000000";

    static LoginCredentialsMap getCredentialsMap() {
        return getCredentialsMap(false);
    }

    static LoginCredentialsMap getCredentialsMap(boolean sync) {
        LoginCredentialsMap loginCredentialsMap = new LoginCredentialsMap();

        HashMap<String, String> loginCredentials = new HashMap<>();
        loginCredentials.put("userid", USER_ID);
        loginCredentials.put("pin", PIN);
        loginCredentialsMap.put(sync ? ACCOUNT_ID_SYNC : ACCOUNT_ID,
                new LoginCredentials(UUID.fromString(PROVIDER_ID), loginCredentials, sync));

        return loginCredentialsMap;
    }

}
