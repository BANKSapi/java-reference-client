package de.banksapi.client;

import de.banksapi.client.crypto.CryptoService;
import de.banksapi.client.crypto.CryptoServiceTest;
import de.banksapi.client.model.incoming.access.*;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;
import de.banksapi.client.model.outgoing.access.Ueberweisung;
import de.banksapi.client.services.CustomerServiceREST;
import de.banksapi.client.services.OAuth2Service;
import de.banksapi.client.services.internal.HttpClient.Response;
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
public class CustomerServiceRESTTest implements BanksapiTest {

    private static CustomerServiceREST customerService;

    private static String bankingAccountId;

    private static Bankzugang bankingAccount;

    private static Bankprodukt bankingProduct;

    private LoginCredentialsMap loginCredentialsMap;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<LoginCredentialsMap> data() {
        return Arrays.asList(getCredentialsMap(false), getCredentialsMap(true));
    }

    public CustomerServiceRESTTest(LoginCredentialsMap loginCredentialsMap) {
        this.loginCredentialsMap = loginCredentialsMap;
    }

    @Before
    public void setUp() throws Exception {
        if (customerService == null) {
            OAuth2Token token = new OAuth2Service().getUserToken(CLIENT_USERNAME, CLIENT_PASSWORD,
                    USERNAME, PASSWORD);
            URL pem = CryptoServiceTest.class.getResource("/public.pem");
            CryptoService cryptoService = CryptoService.fromX509PEMs(pem.getPath());
            customerService = new CustomerServiceREST(token, cryptoService);
        }
    }

    @Test
    public void test010GetCustomer() {
        Response<Customer> response = customerService.getCustomer();
        basicResponseCheckData(response, 200, "get customer");
    }

    @Test
    public void test020GetBankingAccounts() {
        Response<BankzugangMap> response = customerService.getBankzugaenge();
        basicResponseCheckData(response, 200, "get banking accounts");
        assert response.getData().isEmpty() : "list of banking accounts is not empty";
    }

    @Test
    public void test030AddBankingAccount() {
        Response<String> response = customerService.addBankzugaenge(loginCredentialsMap);
        basicResponseCheck(response, 201);
    }

    @Test
    public void test040GetBankingAccounts() {
        Response<BankzugangMap> response = customerService.getBankzugaenge();
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
            Response<KontoumsatzList> response = customerService.getKontoumsaetze(bankingAccountId,
                    bankingProduct.getId());
            basicResponseCheckData(response, 200, "get turnovers");
            assert response.getData().size() > 0 : "no turnovers listed";
        } else if (bankingProduct.hasRelation("get_depotpositionen")) {
            Response<DepotpositionList> response = customerService.getDepotpositionen(bankingAccountId,
                    bankingProduct.getId());
            basicResponseCheckData(response, 200, "get securites");
            assert response.getData().size() > 0 : "no securites listed";
        } else {
            fail("no relation available to get turnovers or securites");
        }
    }

    @Test
    public void test100DeleteBankzugaenge() {
        Response<String> response = customerService.deleteBankzugaenge();
        basicResponseCheck(response, 200);
    }

    @Test
    public void test110GetBankzugang() {
        Response<Bankzugang> response = customerService.getBankzugang(loginCredentialsMap.getFirstAccountId());
        basicResponseCheck(response, 404);
    }

    @Test
    public void test120AddBankzugang() {
        Response<String> response = customerService.addBankzugaenge(loginCredentialsMap);
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
                .withCredentials(loginCredentialsMap.get(loginCredentialsMap.getFirstAccountId()).getCredentials())
                .withTanMediumName("")
                .withSicherheitsverfahrenKodierung("0")
                .build();

        // find suitable banking product that can be used for transfers
        Bankprodukt capableBankingProduct = bankingAccount.getBankprodukte().stream()
                .filter(myBankingProduct -> myBankingProduct.hasRelation("start_ueberweisung"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("account has no products able to " +
                        "perform transfers"));

        UUID providerId = loginCredentialsMap.get(loginCredentialsMap.getFirstAccountId()).getProviderId();
        Response<UeberweisungErgebnis> response = customerService.createUeberweisung(providerId.toString(),
                capableBankingProduct.getId(), transfer);
        basicResponseCheckData(response, 200, "create transfer");
    }

    @Test
    public void test140DeleteBankzugang() {
        Response<String> addResponse = customerService.addBankzugaenge(loginCredentialsMap);
        basicResponseCheck(addResponse, 201);

        Response<Bankzugang> getResponse = customerService.getBankzugang(loginCredentialsMap.getFirstAccountId());
        basicResponseCheck(getResponse, 200);

        Response<String> deleteResponse = customerService.deleteBankzugang(loginCredentialsMap.getFirstAccountId());
        basicResponseCheck(deleteResponse, 200);
    }

}
