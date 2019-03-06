package de.banksapi.client.services;

import de.banksapi.client.crypto.CryptoService;
import de.banksapi.client.model.incoming.access.*;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;
import de.banksapi.client.model.outgoing.access.Ueberweisung;
import de.banksapi.client.services.internal.HttpClient.Response;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static de.banksapi.client.services.internal.HttpHelper.buildUrl;

/**
 * This service interfaces with the Banks/Connect Customer API. This API provides all relevant
 * customer information, such as banking accounts, products and turnovers.
 * <p>This service uses the APIs in the common REST way.</p>
 *
 * @see <a href="https://docs.banksapi.de/customer.html">Banks/Connect Customer API</a>
 */
public class CustomerServiceREST extends CustomerServiceBase {

    /**
     * Creates a new instance of the customer service without encryption capability.
     * <p>When using this constructor credentials provided to
     * {@link #addBankzugaenge(LoginCredentialsMap)} must be encrypted</p>
     *
     * @param oAuth2Token a valid OAuth2 token to send along requests
     */
    public CustomerServiceREST(OAuth2Token oAuth2Token) {
        super(oAuth2Token);
    }

    /**
     * Creates a new instance of the customer service with encryption capability.
     * <p>When using this constructor credentials provided to
     * {@link #addBankzugaenge(LoginCredentialsMap)} will be encrypted on the fly
     * using the provided {@link CryptoService}.</p>
     *
     * @param oAuth2Token a valid OAuth2 token to send along requests
     * @param cryptoService Crypto service to use for credentials encryption
     */
    public CustomerServiceREST(OAuth2Token oAuth2Token, CryptoService cryptoService) {
        super(oAuth2Token, cryptoService);
    }

    public Response<Customer> getCustomer() {
        return createAccessHttpClient(CUSTOMER_CONTEXT).get(Customer.class);
    }

    public Response<BankzugangMap> getBankzugaenge() {
        URL bankzugaengeUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_BANKZUGAENGE);
        return createAccessHttpClient(bankzugaengeUrl).get(BankzugangMap.class);
    }

    public Response<String> addBankzugaenge(LoginCredentialsMap loginCredentialsMap) {
        URL bankzugaengeUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_BANKZUGAENGE);

        LoginCredentialsMap loginCredentialsMapToUse = loginCredentialsMap;
        if (cryptoService != null) {
            loginCredentialsMapToUse = encryptLoginCredentialsMap(loginCredentialsMap);
        }

        return createAccessHttpClient(bankzugaengeUrl)
                .post(loginCredentialsMapToUse, String.class);
    }

    public Response<String> deleteBankzugaenge() {
        URL bankzugaengeUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_BANKZUGAENGE);
        return createAccessHttpClient(bankzugaengeUrl).delete();
    }

    public Response<String> deleteBankzugang(String accountId) {
        URL bankzugangUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_BANKZUGANG, accountId);
        return createAccessHttpClient(bankzugangUrl).delete();
    }

    public Response<Bankzugang> getBankzugang(String accountId) {
        URL bankzugaengeUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_BANKZUGANG, accountId);
        return createAccessHttpClient(bankzugaengeUrl).get(Bankzugang.class);
    }

    public Response<Bankprodukt> getBankprodukt(String accountId, String productId) {
        URL bankzugaengeUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_PRODUKT, accountId, productId);
        return createAccessHttpClient(bankzugaengeUrl).get(Bankprodukt.class);
    }

    public Response<KontoumsatzList> getKontoumsaetze(String accountId, String productId) {
        URL kontoumsaetzeUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_KONTOUMSAETZE, accountId, productId);
        return createAccessHttpClient(kontoumsaetzeUrl).get(KontoumsatzList.class);
    }

    public Response<DepotpositionList> getDepotpositionen(String accountId, String productId) {
        URL depotpositionenUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_DEPOTPOSITIONEN, accountId, productId);
        return createAccessHttpClient(depotpositionenUrl).get(DepotpositionList.class);
    }

    public Response<UeberweisungErgebnis> createUeberweisung(UUID providerId, String productId,
            Ueberweisung ueberweisung) {
        if (cryptoService != null) {
            ueberweisung = new Ueberweisung(ueberweisung, encryptCredentials(ueberweisung.getCredentials()));
        }

        URL ueberweisungUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_UEBERWEISUNG, providerId.toString(), productId);
        return createAccessHttpClient(ueberweisungUrl).post(ueberweisung, UeberweisungErgebnis.class);
    }

    public Response<UeberweisungErgebnis> submitTextTan(UUID providerId, String productId, UUID tanToken,
            String tan) {
        URL submitTanUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_SUBMIT_TAN, providerId.toString(), productId,
                tanToken.toString());

        Map<String, String> submitTanBody = new HashMap<>();
        submitTanBody.put("tan", tan);

        return createAccessHttpClient(submitTanUrl).put(submitTanBody, UeberweisungErgebnis.class);
    }

}
