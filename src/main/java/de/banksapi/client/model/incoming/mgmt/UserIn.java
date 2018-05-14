package de.banksapi.client.model.incoming.mgmt;

import java.util.List;
import java.util.UUID;

public class UserIn {

    private UUID id;
    private String username;
    private List<String> roles;

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
