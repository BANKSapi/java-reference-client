package de.banksapi.client.services;

import de.banksapi.client.crypto.CryptoService;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.access.LoginCredentials;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;
import de.banksapi.client.services.internal.HttpHelper;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.banksapi.client.BANKSapi.getBanksapiBase;

import de.banksapi.client.BANKSapiConfig;

class CustomerServiceBase implements OAuthAwareService {


    final static String PATH_FMT_BANKZUGAENGE = "bankzugaenge";
    final static String PATH_FMT_BANKZUGANG = "bankzugaenge/%s";
    final static String PATH_FMT_PRODUKT = "bankzugaenge/%s/%s";
    final static String PATH_FMT_KONTOUMSAETZE = "bankzugaenge/%s/%s/kontoumsaetze";
    final static String PATH_FMT_DEPOTPOSITIONEN = "bankzugaenge/%s/%s/depotpositionen";
    final static String PATH_FMT_UEBERWEISUNG = "ueberweisung/%s/%s";
    final static String PATH_FMT_SUBMIT_TAN = "ueberweisung/%s/%s/%s";

    private OAuth2Token oAuth2Token;

    CryptoService cryptoService;
    
    private URL customerContext;

    CustomerServiceBase(BANKSapiConfig banksApiConfig, OAuth2Token oAuth2Token) {
        Objects.requireNonNull(banksApiConfig);
        Objects.requireNonNull(oAuth2Token);
        this.oAuth2Token = oAuth2Token;
        this.customerContext = HttpHelper.buildUrl(getBanksapiBase(), "customer/v2/");
    }

    CustomerServiceBase(BANKSapiConfig banksApiConfig, OAuth2Token oAuth2Token,
            CryptoService cryptoService) {
        this(banksApiConfig, oAuth2Token);
        Objects.requireNonNull(cryptoService);
        this.cryptoService = cryptoService;
    }

    public URL getCustomerContext() {
        return customerContext;
    }
    
    @Override
    public OAuth2Token getOAuth2Token() {
        return oAuth2Token;
    }
    
    LoginCredentialsMap encryptLoginCredentialsMap(LoginCredentialsMap loginCredentialsMap) {
        LoginCredentialsMap encryptedLCM = new LoginCredentialsMap();

        loginCredentialsMap.forEach((bankzugangId, loginCredentials) -> {
            Map<String, String> encryptedLC = encryptCredentials(loginCredentials.getCredentials());
            LoginCredentials lc = new LoginCredentials(loginCredentials.getProviderId(), encryptedLC);
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

}
