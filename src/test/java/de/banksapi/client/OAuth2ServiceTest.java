package de.banksapi.client;

import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.services.OAuth2Service;
import org.junit.BeforeClass;
import org.junit.Test;

import static de.banksapi.client.TestAuthData.*;

public class OAuth2ServiceTest {

    private static de.banksapi.client.services.OAuth2Service oAuth2Service;

    @BeforeClass
    public static void setUp() {
        oAuth2Service = new OAuth2Service();
    }

    @Test
    public void testGetClientToken() {
        OAuth2Token token = oAuth2Service.getClientToken(CLIENT_USERNAME, CLIENT_PASSWORD);
        assert token != null : "Unable to retrieve client token";
    }

    @Test
    public void testGetUserToken() {
        OAuth2Token token = oAuth2Service.getUserToken(CLIENT_USERNAME, CLIENT_PASSWORD,
                USERNAME, PASSWORD);
        assert token != null : "Unable to retrieve user token";
    }

}
