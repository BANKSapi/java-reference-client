package de.banksapi.client.model.incoming.mgmt;

import java.util.Collection;

public class Client {

    private String name;
    private String username;
    private String description;
    private Collection<String> grantedScopes;
    private Collection<ClientRole> clientRoles;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public Collection<String> getGrantedScopes() {
        return grantedScopes;
    }

    public Collection<ClientRole> getClientRoles() {
        return clientRoles;
    }
}
