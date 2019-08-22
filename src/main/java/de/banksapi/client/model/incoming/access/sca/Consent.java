package de.banksapi.client.model.incoming.access.sca;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Consent {

    private AccountAccess access;
    private Boolean recurringIndicator;
    private LocalDate validUntil;
    private Integer frequencyPerDay;
    private ConsentStatus consentStatus;

    public AccountAccess getAccess() {
        return access;
    }

    public Boolean getRecurringIndicator() {
        return recurringIndicator;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public Integer getFrequencyPerDay() {
        return frequencyPerDay;
    }

    public ConsentStatus getConsentStatus() {
        return consentStatus;
    }

}
