package de.banksapi.client;

import de.banksapi.client.model.incoming.access.Bankzugang;
import de.banksapi.client.model.incoming.access.BankzugangStatus;
import de.banksapi.client.services.CustomerServiceBase;
import de.banksapi.client.services.internal.HttpClient;

public interface CustomerTest extends BaseTest {

    String BA_CODE_TRANSFER_CREATED = "BA1110";
    String BA_CODE_TAN_SUBMITTED = "BA1111";

    CustomerServiceBase getCustomerService();

    default Bankzugang getBankingAccountWaitComplete(String bankingAccountId) throws InterruptedException {
        boolean complete = false;
        int triesTillFail = 10;
        int pollingInterval = 2000;

        HttpClient.Response<Bankzugang> response = null;
        while (!complete && triesTillFail-- > 0) {
            response = getCustomerService().getBankzugang(bankingAccountId);
            basicResponseCheckData(response, 200, "get banking product while waiting");
            complete = BankzugangStatus.VOLLSTAENDIG.equals(response.getData().getStatus());
            Thread.sleep(pollingInterval);
        }

        assertTrue(complete, "banking account not complete after " + (pollingInterval / 1000) +
                " seconds");
        return response.getData();
    }

}
