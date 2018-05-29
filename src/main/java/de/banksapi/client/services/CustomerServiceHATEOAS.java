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

import static de.banksapi.client.services.internal.HttpHelper.buildUrl;

import de.banksapi.client.BANKSapiConfig;

/**
 * This service interfaces with the Banks/Connect Customer API. This API provides all relevant
 * customer information, such as banking accounts, products and turnovers.
 * <p>This service uses the APIs in the HATEOAS style.</p>
 *
 * @see <a href="https://docs.banksapi.de/customer.html">Banks/Connect Customer API</a>
 */
public class CustomerServiceHATEOAS extends CustomerServiceBase {

    /**
     * Creates a new instance of the customer service without encryption capability.
     * <p>When using this constructor credentials provided to
     * {@link #addBankzugaenge(Customer, LoginCredentialsMap)} must be encrypted</p>
     *
     * @param oAuth2Token a valid OAuth2 token to send along requests
     * @param cryptoService 
     */
    public CustomerServiceHATEOAS(BANKSapiConfig banksApiConfig, OAuth2Token oAuth2Token) {
        super(banksApiConfig, oAuth2Token);
    }

    /**
     * Creates a new instance of the customer service with encryption capability.
     * <p>When using this constructor credentials provided to
     * {@link #addBankzugaenge(Customer, LoginCredentialsMap)} will be encrypted on the fly
     * using the provided {@link CryptoService}.</p>
     *
     * @param oAuth2Token a valid OAuth2 token to send along requests
     * @param cryptoService Crypto service to use for credentials encryption
     */
    public CustomerServiceHATEOAS(BANKSapiConfig banksApiConfig, OAuth2Token oAuth2Token, CryptoService cryptoService) {
        super(banksApiConfig, oAuth2Token, cryptoService);
    }

    public Response<Customer> getCustomer() {
        return createAuthenticatingHttpClient(getCustomerContext()).get(Customer.class);
    }

    public Response<BankzugangMap> getBankzugaenge(Customer customer) {
        return createAuthenticatingHttpClient(customer.getUrlForRelation("get_bankzugaenge"))
                .get(BankzugangMap.class);
    }

    public Response<String> addBankzugaenge(Customer customer, LoginCredentialsMap loginCredentialsMap) {
        LoginCredentialsMap loginCredentialsMapToUse = loginCredentialsMap;
        if (cryptoService != null) {
            loginCredentialsMapToUse = encryptLoginCredentialsMap(loginCredentialsMap);
        }

        return createAuthenticatingHttpClient(customer.getUrlForRelation("add_bankzugaenge"))
                .post(loginCredentialsMapToUse, String.class);
    }

    public Response<String> deleteBankzugaenge(Customer customer) {
        return createAuthenticatingHttpClient(customer.getUrlForRelation("delete_bankzugaenge"))
                .delete();
    }

    public Response<String> deleteBankzugang(Bankzugang account) {
        return createAuthenticatingHttpClient(account.getUrlForRelation("delete_bankzugang"))
                .delete();
    }

    public Response<Bankzugang> getBankzugang(String accountId) {
        URL bankzugaengeUrl = buildUrl(getCustomerContext(), PATH_FMT_BANKZUGANG, accountId);
        return createAuthenticatingHttpClient(bankzugaengeUrl).get(Bankzugang.class);
    }

    public Response<Bankprodukt> getBankprodukt(String accountId, String productId) {
        URL bankzugaengeUrl = buildUrl(getCustomerContext(), PATH_FMT_PRODUKT, accountId, productId);
        return createAuthenticatingHttpClient(bankzugaengeUrl).get(Bankprodukt.class);
    }

    public Response<KontoumsatzList> getKontoumsaetze(Bankprodukt bankprodukt) {
        return createAuthenticatingHttpClient(bankprodukt.getUrlForRelation("get_kontoumsaetze"))
                .get(KontoumsatzList.class);
    }

    public Response<DepotpositionList> getDepotpositionen(Bankprodukt bankprodukt) {
        return createAuthenticatingHttpClient(bankprodukt.getUrlForRelation("get_depotpositionen"))
                .get(DepotpositionList.class);
    }

    public Response<UeberweisungErgebnis> createUeberweisung(Bankprodukt bankprodukt,
            Ueberweisung ueberweisung) {
        if (cryptoService != null) {
            ueberweisung = new Ueberweisung(ueberweisung,
                    encryptCredentials(ueberweisung.getCredentials()));
        }

        return createAuthenticatingHttpClient(bankprodukt.getUrlForRelation("start_ueberweisung"))
                .post(ueberweisung, UeberweisungErgebnis.class);
    }

    public Response<UeberweisungErgebnis> submitTextTan(UeberweisungErgebnis ueberweisungErgebnis,
            String tan) {
        URL submitTanUrl = ueberweisungErgebnis.getUrlForRelation("submit_text_tan");

        Map<String, String> submitTanBody = new HashMap<>();
        submitTanBody.put("tan", tan);

        return createAuthenticatingHttpClient(submitTanUrl)
                .post(submitTanBody, UeberweisungErgebnis.class);
    }


}
