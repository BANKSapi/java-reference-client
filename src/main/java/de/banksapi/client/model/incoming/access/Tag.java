package de.banksapi.client.model.incoming.access;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag {
    private Double confidenceLevel;
    private String displayName;
    private String parent;
    private String systemName;
    private UUID tagId;
    private String tagScope;
    private String tagType;
    private String tagVersion;

    public Double getConfidenceLevel() {
        return confidenceLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getParent() {
        return parent;
    }

    public String getSystemName() {
        return systemName;
    }

    public UUID getTagId() {
        return tagId;
    }

    public String getTagScope() {
        return tagScope;
    }

    public String getTagType() {
        return tagType;
    }

    public String getTagVersion() {
        return tagVersion;
    }
}
