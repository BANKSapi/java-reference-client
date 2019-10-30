package de.banksapi.client.services;

import de.banksapi.client.crypto.CryptoService;
import de.banksapi.client.model.incoming.access.*;
import de.banksapi.client.model.incoming.access.sca.Consent;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.access.ConsentCommand;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;
import de.banksapi.client.model.outgoing.access.Ueberweisung;
import de.banksapi.client.services.internal.HttpClient.Response;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static de.banksapi.client.services.internal.HttpHelper.buildUrl;

/**
 * This service interfaces with the BANKS/Connect Customer API. This API provides all relevant
 * customer information, such as banking accounts, products and turnovers.
 *
 * <p>This service uses the APIs in the HATEOAS style.</p>
 *
 * @see <a href="https://docs.banksapi.de/#banks-connect-customer-api4">Banks/Connect Customer API</a>
 */
public class CustomerServiceHATEOAS extends CustomerServiceBase {

    /**
     * Creates a new instance of the customer service without encryption capability.
     *
     * <p>When using this constructor credentials provided to
     * {@link #addBankzugaenge(Customer, LoginCredentialsMap)} must be encrypted</p>
     *
     * @param oAuth2Token a valid OAuth2 token to send along requests
     */
    public CustomerServiceHATEOAS(OAuth2Token oAuth2Token) {
        super(oAuth2Token);
    }

    /**
     * Creates a new instance of the customer service with encryption capability.
     *
     * <p>When using this constructor credentials provided to
     * {@link #addBankzugaenge(Customer, LoginCredentialsMap)} will be encrypted on the fly
     * using the provided {@link CryptoService}.</p>
     *
     * @param oAuth2Token a valid OAuth2 token to send along requests
     * @param cryptoService Crypto service to use for credentials encryption
     */
    public CustomerServiceHATEOAS(OAuth2Token oAuth2Token, CryptoService cryptoService) {
        super(oAuth2Token, cryptoService);
    }

    public Response<Customer> getCustomer() {
        return createAccessHttpClient(CUSTOMER_CONTEXT).get(Customer.class);
    }

    public Response<BankzugangMap> getBankzugaenge(Customer customer) {
        return createAccessHttpClient(customer.getUrlForRelation("get_bankzugaenge"))
                .get(BankzugangMap.class);
    }

    public Response<String> addBankzugaenge(Customer customer, LoginCredentialsMap loginCredentialsMap) {
        LoginCredentialsMap loginCredentialsMapToUse = loginCredentialsMap;
        if (cryptoService != null) {
            loginCredentialsMapToUse = encryptLoginCredentialsMap(loginCredentialsMap);
        }

        return createAccessHttpClient(customer.getUrlForRelation("add_bankzugaenge"))
                .post(loginCredentialsMapToUse, String.class);
    }

    public Response<String> deleteBankzugaenge(Customer customer) {
        return createAccessHttpClient(customer.getUrlForRelation("delete_bankzugaenge"))
                .delete();
    }

    public Response<String> deleteBankzugang(Bankzugang account) {
        return createAccessHttpClient(account.getUrlForRelation("delete_bankzugang"))
                .delete();
    }

    public Response<Bankzugang> getBankzugang(String accountId) {
        URL bankzugaengeUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_BANKZUGANG, accountId);
        return createAccessHttpClient(bankzugaengeUrl).get(Bankzugang.class);
    }

    public Response<Bankprodukt> getBankprodukt(String accountId, String productId) {
        URL bankzugaengeUrl = buildUrl(CUSTOMER_CONTEXT, PATH_FMT_PRODUKT, accountId, productId);
        return createAccessHttpClient(bankzugaengeUrl).get(Bankprodukt.class);
    }

    public Response<KontoumsatzList> getKontoumsaetze(Bankprodukt bankprodukt) {
        return createAccessHttpClient(bankprodukt.getUrlForRelation("get_kontoumsaetze"))
                .get(KontoumsatzList.class);
    }

    public Response<KontoumsatzKategorisiertList> getKontoumsaetzeKategorisiert(Bankprodukt bankprodukt) {
        return createAccessHttpClient(bankprodukt.getUrlForRelation("get_kontoumsaetze_kategorisiert"))
                .get(KontoumsatzKategorisiertList.class);
    }

    public Response<DepotpositionList> getDepotpositionen(Bankprodukt bankprodukt) {
        return createAccessHttpClient(bankprodukt.getUrlForRelation("get_depotpositionen"))
                .get(DepotpositionList.class);
    }

    public Response<UeberweisungErgebnis> createUeberweisung(Bankprodukt bankprodukt,
            Ueberweisung ueberweisung) {
        if (cryptoService != null) {
            ueberweisung = new Ueberweisung(ueberweisung,
                    encryptCredentials(ueberweisung.getCredentials()));
        }

        return createAccessHttpClient(bankprodukt.getUrlForRelation("start_ueberweisung"))
                .post(ueberweisung, UeberweisungErgebnis.class);
    }

    public Response<UeberweisungErgebnis> submitTextTan(UeberweisungErgebnis ueberweisungErgebnis,
            String tan) {
        URL submitTanUrl = ueberweisungErgebnis.getUrlForRelation("submit_text_tan");

        Map<String, String> submitTanBody = new HashMap<>();
        submitTanBody.put("tan", tan);

        return createAccessHttpClient(submitTanUrl)
                .put(submitTanBody, UeberweisungErgebnis.class);
    }

    public Response<Consent> getConsent(Bankzugang bankzugang) {
        return createAccessHttpClient(bankzugang.getUrlForRelation("get_consent"))
                .get(Consent.class);
    }

    public Response<String> startScaRegProtect(Bankzugang bankzugang) {
        return createAccessHttpClient(bankzugang.getUrlForRelation("start_sca"))
                .get(String.class);
    }

    public Response<String> setScaMethod(Bankzugang bankzugang, ConsentCommand consentCommand) {
        return createAccessHttpClient(bankzugang.getUrlForRelation("set_sca_method"))
                .post(consentCommand, String.class);
    }

    public Response<String> setScaMedium(Bankzugang bankzugang, ConsentCommand consentCommand) {
        return createAccessHttpClient(bankzugang.getUrlForRelation("set_sca_medium"))
                .post(consentCommand, String.class);
    }

    public Response<String> submitAuthentication(Bankzugang bankzugang, ConsentCommand consentCommand) {
        return createAccessHttpClient(bankzugang.getUrlForRelation("authenticate"))
                .put(consentCommand, String.class);
    }

    public Response<String> deleteRegProtectSessions(Customer customer) {
        return createAccessHttpClient(customer.getUrlForRelation("delete_regprotect_sessions"))
                .delete();
    }
}
