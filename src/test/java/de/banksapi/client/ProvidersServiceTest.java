package de.banksapi.client;

import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.incoming.providers.Provider;
import de.banksapi.client.model.incoming.providers.ProviderList;
import de.banksapi.client.services.OAuth2Service;
import de.banksapi.client.services.ProvidersService;
import de.banksapi.client.services.internal.HttpClient.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static de.banksapi.client.TestAuthData.*;

public class ProvidersServiceTest {

    private static ProvidersService providersService;

    @Before
    public void setUp() {
        if (providersService == null) {
            OAuth2Token token = new OAuth2Service().getUserToken(CLIENT_USERNAME, CLIENT_PASSWORD,
                    USERNAME, PASSWORD);
            providersService = new ProvidersService(token);
        }
    }

    @Test
    public void testGetProviders() {
        Response<ProviderList> response = providersService.getProviders();
        assert response.getData().size() > 1 : "provider list not > 1";
    }

    @Test
    public void testGetDemoProvider() {
        Response<Provider> response = providersService.getProvider(
                UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Provider provider = response.getData();

        String demoProviderName = "Demo Provider";
        String demoBic = "DEMO1234";

        assert demoProviderName.equals(provider.getName()) : "provider name != " + demoProviderName;
        assert provider.isConsumerRelevant() : "provider is not consumer relevant";
        assert demoBic.equals(provider.getBic()) : "provider BIC != " + demoBic;
    }

}
