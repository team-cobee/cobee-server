package org.cobee.server.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.alarm.domain.enums.AlarmSourceType;
import org.cobee.server.alarm.domain.enums.AlarmType;
import org.cobee.server.comment.dto.CommentCreatedEvent;
import org.cobee.server.recruit.dto.ApplyAcceptResultEvent;
import org.cobee.server.recruit.dto.ApplyCreatedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.cobee.server.alarm.dto.AlarmCreateRequest;
import org.cobee.server.alarm.service.AlarmService;

@Component
@RequiredArgsConstructor
public class AlarmListener {

    private final AlarmService alarmService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommentCreated(CommentCreatedEvent e) {
        // title과 body는 프론트가 보게 될 메시지 (백엔드가 메시지 구성함)
        var title = e.isReply() ? "대댓글 알림" : "새 댓글 알림";
        var body  = e.isReply() ? "내 댓글에 답글이 달렸어요" : "내 글에 댓글이 달렸어요";

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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplyCreated(ApplyCreatedEvent e) {
        var title = "지원 알림";
        var body  = "새로운 지원이 도착했습니다.";

        alarmService.createAndSend(new AlarmCreateRequest(
                e.getFromUserId(),
                e.getToUserId(),
                AlarmType.NEW_APPLY,
                AlarmSourceType.RECRUIT_POST,
                e.getApplyId(),
                title,
                body
        ));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplyAcceptResult(ApplyAcceptResultEvent e) {
        var title = e.isAccepted() ? "매칭성공" : "매칭거절";
        var body  = e.isAccepted() ? "매칭이 성사되었습니다." : "매칭이 거절되었습니다.";

        alarmService.createAndSend(new AlarmCreateRequest(
                e.getFromUserId(),
                e.getToUserId(),
                AlarmType.START_MATCHING,
                AlarmSourceType.RECRUIT_POST,
                e.getApplyId(),
                title,
                body
        ));
    }
}
