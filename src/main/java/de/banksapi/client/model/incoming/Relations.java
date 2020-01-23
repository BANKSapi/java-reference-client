package de.banksapi.client.model.incoming;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;

public interface Relations {

    Collection<Relation> getRelations();

    default Relation getRelation(String name) {
        Objects.requireNonNull(name);
        return getRelations().stream()
                .filter(relation -> relation.getRel().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No such relation '" + name + "'"));
    }

    default boolean hasRelation(String name) {
        Objects.requireNonNull(name);
        return getRelations().stream()
                .anyMatch(relation -> relation.getRel().equals(name));
    }

    default URL getUrlForRelation(String name) {
        return getUrlForRelation(name, "");
    }

    default URL getUrlForRelation(String name, String suffix) {
        Relation relation = getRelation(name);
        String url = relation.getHref() + suffix;
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid URL in relation: '" + url);
        }
    }

}
