package de.banksapi.client.services.internal;

import java.util.UUID;

public class CorrelationIdHolder {

    private static final InheritableThreadLocal<UUID> uuid = new InheritableThreadLocal<>();

    private CorrelationIdHolder() {}

    public static UUID genAndSet() {
        UUID uuid = UUID.randomUUID();
        CorrelationIdHolder.uuid.set(uuid);
        return uuid;
    }

    public static void remove() {
        uuid.remove();
    }

    public static UUID get() {
        return uuid.get();
    }

}
