package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sicherheitsverfahren {

    private Integer kodierung;
    private String name;
    private String hinweis;

    public Integer getKodierung() {
        return kodierung;
    }

    public String getName() {
        return name;
    }

    public String getHinweis() {
        return hinweis;
    }
}
