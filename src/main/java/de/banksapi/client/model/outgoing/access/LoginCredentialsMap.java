package de.banksapi.client.model.outgoing.access;

import java.util.HashMap;

public class LoginCredentialsMap extends HashMap<String, LoginCredentials> {

    public String getFirstAccountId() {
        return keySet().iterator().next();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        forEach((accountId, loginCredentials) -> {
            if (loginCredentials.isSync()) {
                sb.append("synced ");
            }
            sb.append("credentials (");
            sb.append(accountId);
            sb.append(")");
            sb.append("\n");
        });
        return sb.toString();
    }

}
