package org.cobee.server.member.domain.enums;

public enum SmokingPreference {
    NO_SMOKING("흡연 불가"),
    NO_PREFERENCE("상관 없음");

    private final String displayName;

    SmokingPreference(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}