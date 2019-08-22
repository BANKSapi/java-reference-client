package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Challenge {

    private ChallengeType name;

    private Map<ContentKey, String> content;

    public ChallengeType getName() {
        return name;
    }

    public Map<ContentKey, String> getContent() {
        return content;
    }

    public enum ChallengeType {
        EIN_SCHRITT_TAN_VERFAHREN("1-Schritt-TAN-Verfahren"),
        MOBIL_TAN("mTAN"),
        PHOTO_TAN("photoTAN"),
        CHIPTAN_MANUELL("chipTAN manuell"),
        CHIPTAN_OPTISCH("chipTAN optisch"),
        CHIPTAN_USB("chipTAN-USB"),
        CHIPTAN_QR("chipTAN-QR"),
        DEFAULT_TAN("TAN Verfahren");

        private String name;

        private static Map<String, ChallengeType> lookupMap = new HashMap<>();

        static {
            Arrays.stream(ChallengeType.values()).forEach(ct -> lookupMap.put(ct.getName(), ct));
        }

        ChallengeType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @JsonCreator
        public static ChallengeType fromString(String string) {
            if (lookupMap.containsKey(string)) {
                return lookupMap.get(string);
            } else {
                throw new IllegalArgumentException("unknown challenge type '" + string + "'");
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum ContentKey {
        instructions,
        HHD,
        HHDUC,
        photo,
        decoupled
    }

}
