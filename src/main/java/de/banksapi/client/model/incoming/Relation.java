package de.banksapi.client.model.incoming;

/**
 * Relations are a basic part of the HATEOAS principle of REST APIs.
 */
public class Relation {

    private String rel;
    private String href;

    public String getRel() {
        return rel;
    }

    public String getHref() {
        return href;
    }
}
