package de.banksapi.client.model.outgoing.access;

import java.util.Map;
import java.util.Objects;

/**
 * Class representing input data to induce a money transfer. For instance construction use the
 * accompanying {@link Builder}.
 */
public class Ueberweisung {

    private Map<String, String> credentials;
    private String empfaenger;
    private String verwendungszweck;
    private String iban;
    private String bic;
    private String waehrung;
    private Double betrag;
    private String tanMediumName;
    private String sicherheitsverfahrenKodierung;

    private Ueberweisung() {
    }

    /**
     * Copy constructor allowing to update the credentials.
     *
     * @param ueberweisung the original Ueberweisung to copy
     * @param credentials the updated credentials to be used in the copied object
     */
    public Ueberweisung(Ueberweisung ueberweisung, Map<String, String> credentials) {
        this();
        this.empfaenger = ueberweisung.empfaenger;
        this.verwendungszweck = ueberweisung.verwendungszweck;
        this.iban = ueberweisung.iban;
        this.bic = ueberweisung.bic;
        this.waehrung = ueberweisung.waehrung;
        this.betrag = ueberweisung.betrag;
        this.tanMediumName = ueberweisung.tanMediumName;
        this.sicherheitsverfahrenKodierung = ueberweisung.sicherheitsverfahrenKodierung;
        this.credentials = credentials;
    }

    public Map<String, String> getCredentials() {
        return credentials;
    }

    public String getEmpfaenger() {
        return empfaenger;
    }

    public String getVerwendungszweck() {
        return verwendungszweck;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public Double getBetrag() {
        return betrag;
    }

    public String getTanMediumName() {
        return tanMediumName;
    }

    public String getSicherheitsverfahrenKodierung() {
        return sicherheitsverfahrenKodierung;
    }

    /**
     * Builder to create an {@link Ueberweisung} object.
     */
    public static final class Builder {

        private Ueberweisung ueberweisungToBuild;

        public Builder() {
            ueberweisungToBuild = new Ueberweisung();
        }

        public Builder withCredentials(Map<String, String> credentials) {
            ueberweisungToBuild.credentials = credentials;
            return this;
        }

        public Builder withEmpfaenger(String empfaenger) {
            ueberweisungToBuild.empfaenger = empfaenger;
            return this;
        }

        public Builder withVerwendungszweck(String verwendungszweck) {
            ueberweisungToBuild.verwendungszweck = verwendungszweck;
            return this;
        }

        public Builder withIban(String iban) {
            ueberweisungToBuild.iban = iban;
            return this;
        }

        public Builder withBic(String bic) {
            ueberweisungToBuild.bic = bic;
            return this;
        }

        public Builder withWaehrung(String waehrung) {
            ueberweisungToBuild.waehrung = waehrung;
            return this;
        }

        public Builder withBetrag(double betrag) {
            ueberweisungToBuild.betrag = betrag;
            return this;
        }

        public Builder withTanMediumName(String tanMediumName) {
            ueberweisungToBuild.tanMediumName = tanMediumName;
            return this;
        }

        public Builder withSicherheitsverfahrenKodierung(String sicherheitsverfahrenKodierung) {
            ueberweisungToBuild.sicherheitsverfahrenKodierung = sicherheitsverfahrenKodierung;
            return this;
        }

        public Ueberweisung build() {
            Objects.requireNonNull(ueberweisungToBuild.credentials);
            Objects.requireNonNull(ueberweisungToBuild.empfaenger);
            Objects.requireNonNull(ueberweisungToBuild.verwendungszweck);
            Objects.requireNonNull(ueberweisungToBuild.iban);
            Objects.requireNonNull(ueberweisungToBuild.waehrung);
            Objects.requireNonNull(ueberweisungToBuild.betrag);
            Objects.requireNonNull(ueberweisungToBuild.sicherheitsverfahrenKodierung);
            return ueberweisungToBuild;
        }
    }

}
