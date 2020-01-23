package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.banksapi.client.model.incoming.Messages;
import de.banksapi.client.model.incoming.Relations;

import java.util.Collection;

/**
 * A Bankzugang (banking account) contains all linked banking products {@link Bankprodukt} and
 * informationen about the account's second factor authentication configuration used for
 * transactions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bankzugang extends Issues implements Relations, Messages, ScaAdvice {

    private BankzugangStatus status;
    private Collection<Bankprodukt> bankprodukte;
    private boolean sync;

    public BankzugangStatus getStatus() {
        return status;
    }

    public Collection<Bankprodukt> getBankprodukte() {
        return bankprodukte;
    }

    public boolean isSync() {
        return sync;
    }
}
