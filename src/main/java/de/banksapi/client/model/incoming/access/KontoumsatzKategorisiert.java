package de.banksapi.client.model.incoming.access;

import java.util.Collection;

/**
 * A Kontoumsatz (account turnover) with an additional field for categorization.
 */
public class KontoumsatzKategorisiert extends Kontoumsatz {
    private Collection<Tag> tags;

    public Collection<Tag> getTags() {
        return tags;
    }
}
