package de.banksapi.client.model.incoming.access.sca;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountAccess {

    private List<AccountReference> accounts;
    private List<AccountReference> balances;
    private List<AccountReference> transactions;
    private boolean allAvailable;

    public List<AccountReference> getAccounts() {
        return accounts;
    }

    public List<AccountReference> getBalances() {
        return balances;
    }

    public List<AccountReference> getTransactions() {
        return transactions;
    }

    public boolean isAllAvailable() {
        return allAvailable;
    }

}
