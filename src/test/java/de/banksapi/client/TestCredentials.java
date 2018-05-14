package de.banksapi.client;

import de.banksapi.client.model.outgoing.access.LoginCredentials;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;

import java.util.HashMap;
import java.util.UUID;

/**
 * This file holds credentials used to create banking accounts and money transfers in BANKSapi.
 * <p>Most functionality can be accessed using the preset demo account "demo1".</p>
 */
public class TestCredentials {

    private static final String USER_ID = "demo1";
    private static final String PIN = "demo";
    public static final String ACCOUNT_ID = "demo";
    private static final String PROVIDER_ID = "00000000-0000-0000-0000-000000000000";

    public static LoginCredentialsMap getCredentialsMap() {
        LoginCredentialsMap loginCredentialsMap = new LoginCredentialsMap();

        HashMap<String, String> loginCredentials = new HashMap<>();
        loginCredentials.put("userid", USER_ID);
        loginCredentials.put("pin", PIN);
        loginCredentialsMap.put(ACCOUNT_ID, new LoginCredentials(
                UUID.fromString(PROVIDER_ID), loginCredentials));

        return loginCredentialsMap;
    }

}
