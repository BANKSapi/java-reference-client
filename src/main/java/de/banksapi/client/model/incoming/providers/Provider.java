package de.banksapi.client.model.incoming.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.banksapi.client.model.incoming.Kategorie;
import de.banksapi.client.model.incoming.Relation;
import de.banksapi.client.model.incoming.Relations;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Providers are entities that provide financial accounts that can be accessed by BANKSapi, such
 * as banks, bank like organizations, credit card companies, and so on.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Provider implements Relations {

    private UUID id;
    private String name;
    private String blz;
    private String bic;
    private String group;
    private boolean advancedLogging;
    private ProviderAuthenticationInfo authenticationInfo;
    private boolean consumerRelevant;
    private List<ProviderCapability> capabilities;
    private Set<Set<Kategorie>> channels;
    private List<Relation> relations;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBlz() {
        return blz;
    }

    public String getBic() {
        return bic;
    }

    public String getGroup() {
        return group;
    }

    public boolean isAdvancedLogging() {
        return advancedLogging;
    }

    public ProviderAuthenticationInfo getAuthenticationInfo() {
        return authenticationInfo;
    }

    public boolean isConsumerRelevant() {
        return consumerRelevant;
    }

    public List<ProviderCapability> getCapabilities() {
        return capabilities;
    }

    @Override
    public Collection<Relation> getRelations() {
        return relations;
    }

    public Set<Set<Kategorie>> getChannels() {
        return channels;
    }
}
