package de.banksapi.client.model.incoming.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderAuthenticationInfo {

    private String loginHint;
    private List<ProviderAuthenticationField> fields;

    public String getLoginHint() {
        return loginHint;
    }

    public List<ProviderAuthenticationField> getFields() {
        return fields;
    }

}
