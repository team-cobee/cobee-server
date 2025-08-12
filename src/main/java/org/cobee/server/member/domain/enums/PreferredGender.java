package org.cobee.server.member.domain.enums;

public enum PreferredGender {
    MALE("남자"),
    FEMALE("여자"),
    NO_PREFERENCE("상관없음");

    private final String displayName;

    PreferredGender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}