package de.banksapi.client.model.incoming.access.sca;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ConsentStatus {

    RECEIVED("received"),
    REJECTED("rejected"),
    VALID("valid"),
    REVOKEDBYPSU("revokedByPsu"),
    EXPIRED("expired"),
    TERMINATEDBYTPP("terminatedByTpp");

    private String value;
    private static Map<String, ConsentStatus> lookupMap = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(s -> lookupMap.put(s.value, s));
    }

    ConsentStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ConsentStatus fromValue(String value) {
        if (lookupMap.containsKey(value)) {
            return lookupMap.get(value);
        } else {
            throw new IllegalArgumentException("Unknown ConsentStatus '" + value + "'");
        }
    }

}
