package de.banksapi.client.model.incoming.providers;

import java.util.List;

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
