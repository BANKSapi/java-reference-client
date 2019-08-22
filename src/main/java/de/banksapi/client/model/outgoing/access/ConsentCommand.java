package de.banksapi.client.model.outgoing.access;

/**
 * Data type used to communicate authentication details when performing SCA.
 */
public class ConsentCommand {

    private Integer chosenScaMethodId;
    private String chosenScaMedia;
    private String scaAuthenticationData;

    public Integer getChosenScaMethodId() {
        return chosenScaMethodId;
    }

    public String getChosenScaMedia() {
        return chosenScaMedia;
    }

    public String getScaAuthenticationData() {
        return scaAuthenticationData;
    }

    public static final class ConsentCommandBuilder {
        private ConsentCommand consentCommand;

        public ConsentCommandBuilder() { consentCommand = new ConsentCommand(); }

        public ConsentCommandBuilder withChosenScaMethodId(Integer chosenScaMethodId) {
            consentCommand.chosenScaMethodId = chosenScaMethodId;
            return this;
        }

        public ConsentCommandBuilder withChosenScaMedia(String chosenScaMedia) {
            consentCommand.chosenScaMedia = chosenScaMedia;
            return this;
        }

        public ConsentCommandBuilder withScaAuthenticationData(String scaAuthenticationData) {
            consentCommand.scaAuthenticationData = scaAuthenticationData;
            return this;
        }

        public ConsentCommand build() { return consentCommand; }
    }

}
