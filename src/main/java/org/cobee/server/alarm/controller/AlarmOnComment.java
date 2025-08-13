package org.cobee.server.alarm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.alarm.domain.enums.AlarmSourceType;
import org.cobee.server.alarm.domain.enums.AlarmType;
import org.cobee.server.alarm.dto.AlarmCreateRequest;
import org.cobee.server.alarm.service.AlarmService;
import org.cobee.server.comment.dto.CommentCreatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmOnComment{

    private final AlarmService alarmService;

    // 트랜잭션 커밋 후 실행 (AFTER_COMMIT 기본)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommentCreated(CommentCreatedEvent e) {
        var title = e.isReply() ? "대댓글 알림" : "새 댓글 알림";     // 추측입니다.
        var body  = e.isReply() ? "내 댓글에 답글이 달렸어요!" : "내 글에 댓글이 달렸어요!"; // 추측입니다.

        alarmService.createAndSend(new AlarmCreateRequest(
                e.getFromUserId(),           // fromUserId: 댓글 작성자
                e.getToUserId(),             // toUserId: 수신자
                AlarmType.COMMENT,        // 타입
                AlarmSourceType.COMMENT,  // 출처 타입
                e.getCommentId(),            // 출처 ID(댓글 PK)
                title,
                body
        ));
    }
}
