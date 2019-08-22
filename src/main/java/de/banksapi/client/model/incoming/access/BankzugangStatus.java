package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum BankzugangStatus {
    GESTARTET,
    INTERAKTION,
    SAMMELN,
    VOLLSTAENDIG;

    private static Map<String, BankzugangStatus> lookupMap = new HashMap<>(5);

    static {
        lookupMap.put("GESTARTET", GESTARTET);
        lookupMap.put("INTERAKTION", INTERAKTION);
        lookupMap.put("SAMMELN", SAMMELN);
        lookupMap.put("KOPFDATEN", SAMMELN); // this enables downwards compatibility
        lookupMap.put("VOLLSTAENDIG", VOLLSTAENDIG);
    }

    @JsonCreator
    public static BankzugangStatus forValue(String value) {
        return lookupMap.get(value);
    }
}
