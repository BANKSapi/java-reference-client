package de.banksapi.client.services;

import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.incoming.providers.Provider;
import de.banksapi.client.model.incoming.providers.ProviderList;
import de.banksapi.client.services.internal.HttpClient;
import de.banksapi.client.services.internal.HttpHelper;

import java.net.URL;
import java.util.Objects;
import java.util.UUID;

import static de.banksapi.client.BANKSapi.getBanksapiBase;
import static de.banksapi.client.services.internal.HttpHelper.buildUrl;

/**
 * This service interfaces with the Banks/Connect Providers API. This API is used to retrieve
 * information about {@link Provider}s.
 *
 * @see <a href="https://docs.banksapi.de/providers.html">Banks/Connect Providers API</a>
 */
public class ProvidersService implements OAuthAwareService {

    private final static URL PROVIDERS_CONTEXT = HttpHelper.buildUrl(getBanksapiBase(),
            "providers/v2/");

    private OAuth2Token oAuth2Token;

    /**
     * Creates a new instance of the providers service.
     *
     * @param oAuth2Token a valid OAuth2 token to send along all requests
     */
    public ProvidersService(OAuth2Token oAuth2Token) {
        Objects.requireNonNull(oAuth2Token);
        this.oAuth2Token = oAuth2Token;
    }

    public HttpClient.Response<ProviderList> getProviders() {
        return createAuthenticatingHttpClient(PROVIDERS_CONTEXT).get(ProviderList.class);
    }

    public HttpClient.Response<Provider> getProvider(UUID providerId) {
        URL providerUrl = buildUrl(PROVIDERS_CONTEXT, providerId.toString());
        return createAuthenticatingHttpClient(providerUrl).get(Provider.class);
    }

    @Override
    public OAuth2Token getOAuth2Token() {
        return oAuth2Token;
    }

}
