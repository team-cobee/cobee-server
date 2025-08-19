package org.cobee.server.recruit.domain.enums;

public enum MatchStatus {
    ON_WAIT,     // 지원했을 때 승인 받는거 대기중
    MATCHING, // 승인 받음
    REJECTED, // 거절당함
    MATCHED  // 최종매칭
}
