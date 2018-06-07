package de.banksapi.client.services.internal;

import java.util.regex.Pattern;

public class StringUtil {

    private static Pattern strWithVisibleCharacters = Pattern.compile(".*\\p{Graph}.*");

    /**
     * Checks if the given string is null, empty or consists only of invisible characters.
     *
     * @param string String to check
     * @return true if the given string is null, empty or consists only of invisible characters, false otherwise.
     */
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
