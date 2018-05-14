package de.banksapi.client.services.internal;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpHelper {

    public static URL buildUrl(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Malformed URL '" + spec + "'", e);
        }
    }

    public static URL buildUrl(URL context, String spec) {
        try {
            return new URL(context, spec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to build URL", e);
        }
    }

    public static URL buildUrl(URL context, String pathFmt, String... pathParts) {
        String path;
        if (pathParts == null) {
            path = pathFmt;
        } else {
            path = String.format(pathFmt, (Object[]) pathParts);
        }
        return buildUrl(context, path);
    }
}
