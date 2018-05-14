package de.banksapi.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class BANKSapi {

    private static Pattern strWithVisibleCharacters = Pattern.compile(".*\\p{Graph}.*");

    private static URL BANKSAPI_BASE_URL;

    public static URL getBanksapiBase() {
        if (BANKSAPI_BASE_URL == null) {
            String url = System.getProperty("BANKSAPI_BASE_URL");
            url = isBlank(url) ? "https://banksapi.io" : url;
            try {
                BANKSAPI_BASE_URL = new URL(url);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Invalid URL '" + url + "'", e);
            }
        }

        return BANKSAPI_BASE_URL;
    }

    public static boolean isBlank(String string) {
        if (string == null) {
            return true;
        } else if (string.isEmpty()) {
            return true;
        } else if (!strWithVisibleCharacters.matcher(string).matches()) {
            return true;
        } else {
            return false;
        }
    }

}
