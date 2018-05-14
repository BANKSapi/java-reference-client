package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.banksapi.client.model.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

public class TanMedium {

    private String name;
    private Medienklasse medienklasse;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime gueltigVon;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime gueltigBis;

    public String getName() {
        return name;
    }

    public Medienklasse getMedienklasse() {
        return medienklasse;
    }

    public LocalDateTime getGueltigVon() {
        return gueltigVon;
    }

    public LocalDateTime getGueltigBis() {
        return gueltigBis;
    }
}
