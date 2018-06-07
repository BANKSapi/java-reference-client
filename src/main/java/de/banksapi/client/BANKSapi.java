package de.banksapi.client;

import java.net.MalformedURLException;
import java.net.URL;

import static de.banksapi.client.services.internal.StringUtil.isBlank;

/**
 * Initializes the BANKSapi base URL to be used by the reference client.
 */
public class BANKSapi {

    private static URL banksapiBaseUrl;

    /**
     * Retrieves the BANKSapi base URL.
     * <p>If no URL has been set explicitly using {@link #init(URL)} the system property {@code BANKSAPI_BASE_URL} is
     * used implicitly.</p>
     *
     * @return BANKSapi base URL
     */
    public static URL getBanksapiBase() {
        if (banksapiBaseUrl == null) {
            initFromProperty();
        }

        return banksapiBaseUrl;
    }

    private static void initFromProperty() {
        String url = System.getProperty("BANKSAPI_BASE_URL");
        url = isBlank(url) ? "https://banksapi.io" : url;
        try {
            banksapiBaseUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid URL '" + url + "'", e);
        }
    }

    /**
     * Initializes the base URL with the given parameter.
     *
     * @param banksapiBaseUrl BANKSapi base URL
     */
    public static void init(URL banksapiBaseUrl) {
        if (BANKSapi.banksapiBaseUrl == null) {
            BANKSapi.banksapiBaseUrl = banksapiBaseUrl;
        } else {
            throw new IllegalStateException("init(URL) called but base URL is already initialized");
        }
    }

}
