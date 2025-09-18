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
        // 누군가가 내 글에 댓글을 달았을때
        alarmService.createAndSend(new AlarmCreateRequest(
                e.getFromUserId(),           // fromUserId: 댓글 작성자
                e.getToUserId(),             // toUserId: 수신자
                AlarmType.COMMENT,        // 타입
                AlarmSourceType.COMMENT,  // 출처 타입
                e.getCommentId()          // 출처 ID(댓글 PK)
        ));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplyCreated(ApplyCreatedEvent e) {
        // 누군가가 지원을 나한테 했을때

        alarmService.createAndSend(new AlarmCreateRequest(
                e.getFromUserId(),
                e.getToUserId(),
                AlarmType.NEW_APPLY,
                AlarmSourceType.RECRUIT_POST,
                e.getApplyId()
        ));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplyAcceptResult(ApplyAcceptResultEvent e) {

        // 구인글 주인이 승인을 했을 때,
        alarmService.createAndSend(new AlarmCreateRequest(
                e.getFromUserId(),
                e.getToUserId(),
                AlarmType.START_MATCHING,
                AlarmSourceType.RECRUIT_POST,
                e.getApplyId()
        ));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onChatCreated(ApplyAcceptResultEvent e) {

        // 구인글 주인이 승인을 했을 때,
        alarmService.createAndSend(new AlarmCreateRequest(
                e.getFromUserId(),
                e.getToUserId(),
                AlarmType.START_MATCHING,
                AlarmSourceType.RECRUIT_POST,
                e.getApplyId()
        ));
    }
}
