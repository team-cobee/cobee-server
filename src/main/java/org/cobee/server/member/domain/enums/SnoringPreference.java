package org.cobee.server.member.domain.enums;

public enum SnoringPreference {
    NO_PREFERENCE("상관 없음"),
    NO_SNORING("코골이 불가");

    private final String displayName;

    SnoringPreference(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}