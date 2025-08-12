package org.cobee.server.member.domain.enums;

public enum PetPreference {
    POSSIBLE("가능"),
    IMPOSSIBLE("불가능"),
    NO_PREFERENCE("상관 없음");

    private final String displayName;

    PetPreference(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}