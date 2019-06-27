package de.banksapi.client.services;

import de.banksapi.client.crypto.CryptoService;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.access.LoginCredentials;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;
import de.banksapi.client.services.internal.HttpClient;
import de.banksapi.client.services.internal.HttpHelper;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.banksapi.client.BANKSapi.getBanksapiBase;

class CustomerServiceBase implements OAuthAwareService {

    final static URL CUSTOMER_CONTEXT = HttpHelper.buildUrl(getBanksapiBase(), "customer/v2/");

    final static String PATH_FMT_BANKZUGAENGE = "bankzugaenge";
    final static String PATH_FMT_BANKZUGANG = "bankzugaenge/%s";
    final static String PATH_FMT_PRODUKT = "bankzugaenge/%s/%s";
    final static String PATH_FMT_KONTOUMSAETZE = "bankzugaenge/%s/%s/kontoumsaetze";
    final static String PATH_FMT_KONTOUMSAETZE_KATEGORISIERT = "bankzugaenge/%s/%s/kontoumsaetze?categorize=true";
    final static String PATH_FMT_DEPOTPOSITIONEN = "bankzugaenge/%s/%s/depotpositionen";
    final static String PATH_FMT_UEBERWEISUNG = "ueberweisung/%s/%s";
    final static String PATH_FMT_SUBMIT_TAN = "ueberweisung/%s/%s/%s";

    private OAuth2Token oAuth2Token;

    CryptoService cryptoService;

    CustomerServiceBase(OAuth2Token oAuth2Token) {
        Objects.requireNonNull(oAuth2Token);
        this.oAuth2Token = oAuth2Token;
    }

    CustomerServiceBase(OAuth2Token oAuth2Token,
            CryptoService cryptoService) {
        this(oAuth2Token);
        Objects.requireNonNull(cryptoService);
        this.cryptoService = cryptoService;
    }

    @Override
    public OAuth2Token getOAuth2Token() {
        return oAuth2Token;
    }

    LoginCredentialsMap encryptLoginCredentialsMap(LoginCredentialsMap loginCredentialsMap) {
        LoginCredentialsMap encryptedLCM = new LoginCredentialsMap();

        loginCredentialsMap.forEach((bankzugangId, loginCredentials) -> {
            Map<String, String> encryptedLC = encryptCredentials(loginCredentials.getCredentials());
            LoginCredentials lc = new LoginCredentials(loginCredentials.getProviderId(), encryptedLC,
                    loginCredentials.isSync());
            encryptedLCM.put(bankzugangId, lc);
        });

        return encryptedLCM;
    }

    Map<String, String> encryptCredentials(Map<String, String> credentials) {
        return credentials.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> cryptoService.encryptToBase64String(entry.getValue())));
    }

    HttpClient createAccessHttpClient(URL url) {
        return createAuthenticatingHttpClient(url).setConnectTimeout(30000).setReadTimeout(285000);
    }

}
