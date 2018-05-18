package de.banksapi.client;

import java.net.URL;
import java.util.Objects;

/**
 * Configuration for the targeted deployed BANKSapi service
 *
 */
public class BANKSapiConfig {

    private final URL banksapiBase;

    public BANKSapiConfig(final URL banksapiBase) {
        Objects.requireNonNull(banksapiBase);
        this.banksapiBase = banksapiBase;
    }

    public URL getBanksapiBase() {
        return banksapiBase;
    }
}
