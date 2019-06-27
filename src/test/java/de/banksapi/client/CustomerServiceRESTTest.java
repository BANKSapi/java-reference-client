package de.banksapi.client;

import de.banksapi.client.crypto.CryptoService;
import de.banksapi.client.crypto.CryptoServiceTest;
import de.banksapi.client.model.incoming.access.*;
import de.banksapi.client.model.incoming.oauth2.OAuth2Token;
import de.banksapi.client.model.outgoing.access.LoginCredentialsMap;
import de.banksapi.client.model.outgoing.access.Ueberweisung;
import de.banksapi.client.services.CustomerServiceREST;
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
public class CustomerServiceRESTTest implements BanksapiTest {

    private static CustomerServiceREST customerService;

    private static String bankingAccountId;

    private static Bankzugang bankingAccount;

    private static Bankprodukt bankingProduct;

    private static UUID providerId;

    private static UeberweisungErgebnis transferResult;

    private LoginCredentialsMap loginCredentialsMap;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<LoginCredentialsMap> data() {
        return Arrays.asList(getCredentialsMap(false), getCredentialsMap(true));
        //return Arrays.asList(getCredentialsMap(true));
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

    @After
    public void tearDown() {
        CorrelationIdHolder.remove();
    }

    @Test
    public void test010GetCustomer() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<Customer> response = customerService.getCustomer();
        basicResponseCheckData(response, 200, "get customer", cid);
    }

    @Test
    public void test020GetBankingAccounts() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<BankzugangMap> response = customerService.getBankzugaenge();
        basicResponseCheckData(response, 200, "get banking accounts", cid);
        assert response.getData().isEmpty() : "list of banking accounts is not empty";
    }

    @Test
    public void test030AddBankingAccount() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<String> response = customerService.addBankzugaenge(loginCredentialsMap);
        basicResponseCheck(response, 201, cid);
    }

    @Test
    public void test040GetBankingAccounts() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<BankzugangMap> response = customerService.getBankzugaenge();
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
        UUID cid = CorrelationIdHolder.genAndSet();
        boolean complete = false;
        int triesTillFail = 10;
        int pollingInterval = 2000;

        Response<Bankzugang> response = null;
        while (!complete && triesTillFail-- > 0) {
            response = customerService.getBankzugang(bankingAccountId);
            basicResponseCheckData(response, 200, "get banking product while waiting", cid);
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
            Response<KontoumsatzList> response = customerService.getKontoumsaetze(bankingAccountId,
                    bankingProduct.getId());
            basicResponseCheckData(response, 200, "get turnovers", cid);
            assert response.getData().size() > 0 : "no turnovers listed";
        } else if (bankingProduct.hasRelation("get_depotpositionen")) {
            Response<DepotpositionList> response = customerService.getDepotpositionen(bankingAccountId,
                    bankingProduct.getId());
            basicResponseCheckData(response, 200, "get securities", cid);
            assert response.getData().size() > 0 : "no securities listed";
        } else {
            fail("no relation available to get turnovers or securities");
        }
    }

    @Test
    public void test091GetKontoumsaetzeKategorisiert() {
        UUID cid = CorrelationIdHolder.genAndSet();
        if (bankingProduct.hasRelation("get_kontoumsaetze_kategorisiert")) {
            Response<KontoumsatzKategorisiertList> response = customerService
                    .getKontoumsaetzeKategorisiert(bankingAccountId,
                    bankingProduct.getId());
            basicResponseCheckData(response, 200, "get turnovers categorized", cid);
            assert response.getData().size() > 0 : "no turnovers listed";
        } else {
            fail("no relation available to get categorised turnovers. " +
                    "Tenant may not be eligible for categorization");
        }
    }

    @Test
    public void test100DeleteBankzugaenge() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<String> response = customerService.deleteBankzugaenge();
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
        Response<String> response = customerService.addBankzugaenge(loginCredentialsMap);
        basicResponseCheck(response, 201, cid);
    }

    @Test
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
                .withSicherheitsverfahrenKodierung("1")
                .build();

        // find suitable banking product that can be used for transfers
        Bankprodukt capableBankingProduct = bankingAccount.getBankprodukte().stream()
                .filter(myBankingProduct -> myBankingProduct.hasRelation("start_ueberweisung"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("account has no products able to " +
                        "perform transfers"));

        providerId = loginCredentialsMap.get(loginCredentialsMap.getFirstAccountId()).getProviderId();
        Response<UeberweisungErgebnis> response = customerService.createUeberweisung(providerId,
                capableBankingProduct.getId(), transfer);
        basicResponseCheckData(response, 200, "create transfer", cid);

        assert response.getData().containsMessageCode(BA_CODE_TRANSFER_CREATED) :
                "create transfer result does not contain message code " + BA_CODE_TRANSFER_CREATED;

        transferResult = response.getData();
    }

    @Test
    public void test140SubmitTan() {
        UUID cid = CorrelationIdHolder.genAndSet();

        UUID tanToken = null;
        try {
            String[] parts = transferResult.getRelation("submit_text_tan").getHref().split("/");
            tanToken = UUID.fromString(parts[parts.length - 1]);
        } catch (Exception e) {
            fail("Unable to get TAN token from relation: " + e.getMessage());
        }

        Response<UeberweisungErgebnis> response = customerService.submitTextTan(providerId,
                bankingProduct.getId(), tanToken, "42");
        basicResponseCheckData(response, 200, "submit tan", cid);

        assert response.getData().containsMessageCode(BA_CODE_TAN_SUBMITTED) :
                "submit tan result does not contain message code " + BA_CODE_TAN_SUBMITTED;
    }

    @Test
    public void test150DeleteBankzugang() {
        UUID cid = CorrelationIdHolder.genAndSet();
        Response<String> addResponse = customerService.addBankzugaenge(loginCredentialsMap);
        basicResponseCheck(addResponse, 201, cid);

        cid = CorrelationIdHolder.genAndSet();
        Response<Bankzugang> getResponse = customerService.getBankzugang(loginCredentialsMap.getFirstAccountId());
        basicResponseCheck(getResponse, 200, cid);

        cid = CorrelationIdHolder.genAndSet();
        Response<String> deleteResponse = customerService.deleteBankzugang(loginCredentialsMap.getFirstAccountId());
        basicResponseCheck(deleteResponse, 200, cid);
    }

}
