package de.banksapi.client.model.incoming.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderAuthenticationField {

    private String fieldkey;
    private String label;
    private Boolean secret;
    private String hint;
    private String format;
    private Integer maxLengthHbci;

    public String getFieldkey() {
        return fieldkey;
    }

    public String getLabel() {
        return label;
    }

    public Boolean getSecret() {
        return secret;
    }

    public String getHint() {
        return hint;
    }

    public String getFormat() {
        return format;
    }

    public Integer getMaxLengthHbci() {
        return maxLengthHbci;
    }

}
