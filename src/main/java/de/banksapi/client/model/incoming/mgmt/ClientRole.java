package de.banksapi.client.model.incoming.mgmt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientRole {

    private String name; // unique
    private String description;
    private Boolean defaultRole;
    private Collection<String> grantedScopes;
    private Collection<UUID> users;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDefaultRole() {
        return defaultRole;
    }

    public Collection<String> getGrantedScopes() {
        return grantedScopes;
    }

    public Collection<UUID> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRole that = (ClientRole) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
