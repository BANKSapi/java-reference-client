package de.banksapi.client;

import de.banksapi.client.crypto.CryptoService;
import de.banksapi.client.crypto.CryptoServiceTest;
import de.banksapi.client.model.incoming.access.*;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.access.Ueberweisung;
import de.banksapi.client.services.CustomerServiceHATEOAS;
import de.banksapi.client.services.OAuth2Service;
import de.banksapi.client.services.internal.HttpClient.Response;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URL;

import static de.banksapi.client.TestAuthData.*;
import static de.banksapi.client.TestCredentials.ACCOUNT_ID;
import static junit.framework.TestCase.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerServiceHATEOASTest implements BanksapiTest {

    private static CustomerServiceHATEOAS customerService;

    private static Customer customer;

    private static String bankingAccountId;

    private static Bankzugang bankingAccount;

    private static Bankprodukt bankingProduct;

    @BeforeClass
    public static void setUp() throws Exception {
        OAuth2Token token = new OAuth2Service().getUserToken(CLIENT_USERNAME, CLIENT_PASSWORD,
                USERNAME, PASSWORD);
        URL keystore = CryptoServiceTest.class.getResource("/keystore.jks");
        CryptoService cryptoService = CryptoService.fromKeystore(keystore.getPath(), "demo");
        customerService = new CustomerServiceHATEOAS(token, cryptoService);
    }

    @Test
    public void test010GetCustomer() {
        Response<Customer> response = customerService.getCustomer();
        basicResponseCheckData(response, 200, "get customer");
        customer = response.getData();
    }

    @Test
    public void test020GetBankingAccounts() {
        Response<BankzugangMap> response = customerService.getBankzugaenge(customer);
        basicResponseCheckData(response, 200, "get banking accounts");
        assert response.getData().isEmpty() : "list of banking accounts is not empty";
    }

    @Test
    public void test030AddBankingAccount() {
        Response<String> response = customerService.addBankzugaenge(customer,
                TestCredentials.getCredentialsMap());
        basicResponseCheck(response, 201);
    }

    @Test
    public void test040GetBankingAccounts() {
        Response<BankzugangMap> response = customerService.getBankzugaenge(customer);
        basicResponseCheckData(response, 200, "get banking accounts");

        assert !response.getData().isEmpty() : "list of banking accounts is empty";
        bankingAccountId = response.getData().keySet().iterator().next();
    }

    @Test
    public void test050GetBankingAccount() {
        Response<Bankzugang> response = customerService.getBankzugang(bankingAccountId);
        basicResponseCheckData(response, 200, "get banking account");
        assert !response.getData().getBankprodukte().isEmpty() : "list of banking products is empty";
    }

    @Test
    public void test060GetBankingAccountWaitComplete() throws InterruptedException {
        boolean complete = false;
        int triesTillFail = 10;
        int pollingInterval = 2000;

        Response<Bankzugang> response = null;
        while (!complete && triesTillFail-- > 0) {
            response = customerService.getBankzugang(bankingAccountId);
            complete = BankzugangStatus.VOLLSTAENDIG.equals(response.getData().getStatus());
            Thread.sleep(pollingInterval);
        }

        assert complete : "banking account not complete after " + (pollingInterval / 1000) +
                " seconds";
        bankingAccount = response.getData();
    }

    @Test
    public void test070BankingAccountHasBankingProducts() {
        assert !bankingAccount.getBankprodukte().isEmpty() : "banking account does not have " +
                "banking products";
        bankingProduct = bankingAccount.getBankprodukte().iterator().next();
    }

    @Test
    public void test080GetBankprodukt() {
        Response<Bankprodukt> response = customerService.getBankprodukt(bankingAccountId,
                bankingProduct.getId());
        basicResponseCheckData(response, 200, "get banking product");
    }

    @Test
    public void test090GetKontoumsaetzeOrDepotpositionen() {
        if (bankingProduct.hasRelation("get_kontoumsaetze")) {
            Response<KontoumsatzList> response = customerService.getKontoumsaetze(bankingProduct);
            basicResponseCheckData(response, 200, "get turnovers");
            assert response.getData().size() > 0 : "no turnovers listed";
        } else if (bankingProduct.hasRelation("get_depotpositionen")) {
            Response<DepotpositionList> response = customerService.getDepotpositionen(bankingProduct);
            basicResponseCheckData(response, 200, "get securites");
            assert response.getData().size() > 0 : "no securites listed";
        } else {
            fail("no relation available to get turnovers or securites");
        }
    }

    @Test
    public void test100DeleteBankzugaenge() {
        Response<String> response = customerService.deleteBankzugaenge(customer);
        basicResponseCheck(response, 200);
    }

    @Test
    public void test110GetBankzugang() {
        Response<Bankzugang> response = customerService.getBankzugang(ACCOUNT_ID);
        basicResponseCheck(response, 404);
    }

    @Test
    public void test120AddBankzugang() {
        Response<String> response = customerService.addBankzugaenge(customer,
                TestCredentials.getCredentialsMap());
        basicResponseCheck(response, 201);
    }

    //@Test
    public void test130CreateUeberweisung() {
        Ueberweisung transfer = new Ueberweisung.Builder()
                .withBetrag(0.01)
                .withWaehrung("EUR")
                .withEmpfaenger("Jane Doe")
                .withVerwendungszweck("Test")
                .withIban("DE44500105175407324931")
                .withCredentials(TestCredentials.getCredentialsMap().get(ACCOUNT_ID).getCredentials())
                .withTanMediumName("")
                .withSicherheitsverfahrenKodierung("0")
                .build();

        // find suitable banking product that can be used for transfers
        Bankprodukt capableBankingProduct = bankingAccount.getBankprodukte().stream()
                .filter(myBankingProduct -> myBankingProduct.hasRelation("start_ueberweisung"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("account has no products able to " +
                        "perform tranfers"));

        Response<UeberweisungErgebnis> response = customerService.createUeberweisung(
                capableBankingProduct, transfer);
        basicResponseCheckData(response, 200, "create transfer");
    }

    @Test
    public void test140DeleteBankzugang() {
        Response<String> addResponse = customerService.addBankzugaenge(customer,
                TestCredentials.getCredentialsMap());
        basicResponseCheck(addResponse, 201);

        Response<Bankzugang> getResponse = customerService.getBankzugang(ACCOUNT_ID);
        basicResponseCheck(getResponse, 200);

        Response<String> deleteResponse = customerService.deleteBankzugang(getResponse.getData());
        basicResponseCheck(deleteResponse, 200);
    }

}
