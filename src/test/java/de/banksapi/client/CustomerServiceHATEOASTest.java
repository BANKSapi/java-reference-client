package de.banksapi.client;

import de.banksapi.client.crypto.CryptoService;
import de.banksapi.client.crypto.CryptoServiceTest;
import de.banksapi.client.model.incoming.access.*;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;
import de.banksapi.client.model.outgoing.access.Ueberweisung;
import de.banksapi.client.services.CustomerServiceHATEOAS;
import de.banksapi.client.services.OAuth2Service;
import de.banksapi.client.services.internal.CorrelationIdHolder;
import de.banksapi.client.services.internal.HttpClient.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static de.banksapi.client.TestAuthData.*;
import static de.banksapi.client.TestCredentials.getCredentialsMap;
import static junit.framework.TestCase.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class CustomerServiceHATEOASTest implements BanksapiTest {

    private static CustomerServiceHATEOAS customerService;

    private static Customer customer;

    private static String bankingAccountId;

    private static Bankzugang bankingAccount;

    private static Bankprodukt bankingProduct;

    private LoginCredentialsMap loginCredentialsMap;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<LoginCredentialsMap> data() {
        return Arrays.asList(getCredentialsMap(false), getCredentialsMap(true));
    }

    public CustomerServiceHATEOASTest(LoginCredentialsMap loginCredentialsMap) {
        this.loginCredentialsMap = loginCredentialsMap;
    }

    @Before
    public void setUp() throws Exception {
        if (customerService == null) {
            OAuth2Token token = new OAuth2Service().getUserToken(CLIENT_USERNAME, CLIENT_PASSWORD,
                    USERNAME, PASSWORD);
            URL pem = CryptoServiceTest.class.getResource("/public.pem");
            CryptoService cryptoService = CryptoService.fromX509PEMs(pem.getPath());
            customerService = new CustomerServiceHATEOAS(token, cryptoService);
        }
    }

    @After
    public void tearDown() {
        CorrelationIdHolder.remove();
    }

    @Test
    public void test010GetCustomer() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<Customer> response = customerService.getCustomer();
        basicResponseCheckData(response, 200, "get customer", cid);
        customer = response.getData();
    }

    @Test
    public void test020GetBankingAccounts() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<BankzugangMap> response = customerService.getBankzugaenge(customer);
        basicResponseCheckData(response, 200, "get banking accounts", cid);
        assert response.getData().isEmpty() : "list of banking accounts is not empty";
    }

    @Test
    public void test030AddBankingAccount() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<String> response = customerService.addBankzugaenge(customer, loginCredentialsMap);
        basicResponseCheck(response, 201, cid);
    }

    @Test
    public void test040GetBankingAccounts() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<BankzugangMap> response = customerService.getBankzugaenge(customer);
        basicResponseCheckData(response, 200, "get banking accounts", cid);

        assert !response.getData().isEmpty() : "list of banking accounts is empty";
        bankingAccountId = response.getData().keySet().iterator().next();
    }

    @Test
    public void test050GetBankingAccount() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<Bankzugang> response = customerService.getBankzugang(bankingAccountId);
        basicResponseCheckData(response, 200, "get banking account", cid);
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
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<Bankprodukt> response = customerService.getBankprodukt(bankingAccountId,
                bankingProduct.getId());
        basicResponseCheckData(response, 200, "get banking product", cid);
    }

    @Test
    public void test090GetKontoumsaetzeOrDepotpositionen() {
        UUID cid = CorrelationIdHolder.genAndSet();
        if (bankingProduct.hasRelation("get_kontoumsaetze")) {
            Response<KontoumsatzList> response = customerService.getKontoumsaetze(bankingProduct);
            basicResponseCheckData(response, 200, "get turnovers", cid);
            assert response.getData().size() > 0 : "no turnovers listed";
        } else if (bankingProduct.hasRelation("get_depotpositionen")) {
            Response<DepotpositionList> response = customerService.getDepotpositionen(bankingProduct);
            basicResponseCheckData(response, 200, "get securites", cid);
            assert response.getData().size() > 0 : "no securites listed";
        } else {
            fail("no relation available to get turnovers or securites");
        }
    }

    @Test
    public void test100DeleteBankzugaenge() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<String> response = customerService.deleteBankzugaenge(customer);
        basicResponseCheck(response, 200, cid);
    }

    @Test
    public void test110GetBankzugang() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<Bankzugang> response = customerService.getBankzugang(loginCredentialsMap.getFirstAccountId());
        basicResponseCheck(response, 404, cid);
    }

    @Test
    public void test120AddBankzugang() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<String> response = customerService.addBankzugaenge(customer, loginCredentialsMap);
        basicResponseCheck(response, 201, cid);
    }

    //@Test
    public void test130CreateUeberweisung() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Ueberweisung transfer = new Ueberweisung.Builder()
                .withBetrag(0.01)
                .withWaehrung("EUR")
                .withEmpfaenger("Jane Doe")
                .withVerwendungszweck("Test")
                .withIban("DE44500105175407324931")
                .withCredentials(loginCredentialsMap.get(loginCredentialsMap.getFirstAccountId()).getCredentials())
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
        basicResponseCheckData(response, 200, "create transfer", cid);
    }

    @Test
    public void test140DeleteBankzugang() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<String> addResponse = customerService.addBankzugaenge(customer, loginCredentialsMap);
        basicResponseCheck(addResponse, 201, cid);

        cid = CorrelationIdHolder.genAndSet();
        Response<Bankzugang> getResponse = customerService.getBankzugang(loginCredentialsMap.getFirstAccountId());
        basicResponseCheck(getResponse, 200, cid);

        cid = CorrelationIdHolder.genAndSet();
        Response<String> deleteResponse = customerService.deleteBankzugang(getResponse.getData());
        basicResponseCheck(deleteResponse, 200, cid);
    }

}
