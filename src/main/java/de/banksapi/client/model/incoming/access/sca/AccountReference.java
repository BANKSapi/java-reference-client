package de.banksapi.client.model.incoming.access.sca;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountReference {

    private String iban;
    private String currency;

    public String getIban() {
        return iban;
    }

    public String getCurrency() {
        return currency;
    }
}
