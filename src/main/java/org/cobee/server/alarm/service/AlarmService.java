package org.cobee.server.alarm.service;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.alarm.domain.Alarm;
import org.cobee.server.alarm.domain.AlarmNotice;
import org.cobee.server.alarm.dto.AlarmCreateRequest;
import org.cobee.server.alarm.dto.AlarmNoticeResponse;
import org.cobee.server.alarm.repository.AlarmNoticeRepository;
import org.cobee.server.alarm.repository.AlarmRepository;
import org.cobee.server.alarm.fcm.FcmSender;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final AlarmNoticeRepository alarmNoticeRepository;
    private final MemberRepository memberRepository;
    private final FcmSender fcmSender;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AlarmNoticeResponse createAndSend(AlarmCreateRequest req) {
        Member from = memberRepository.findById(req.getFromUserId())
                .orElseThrow(() -> new IllegalArgumentException("fromUser not found"));
        Member to = memberRepository.findById(req.getToUserId())
                .orElseThrow(() -> new IllegalArgumentException("toUser not found"));

        Alarm alarm = Alarm.builder()
                .alarmType(req.getAlarmType())
                .sourceType(req.getSourceType())
                .sourceId(req.getSourceId())
                .member(from)
                .build();
        alarmRepository.save(alarm);

        AlarmNotice notice = AlarmNotice.builder()
                .alarm(alarm)
                .member(to)
                .isRead(false)
                .build();
        alarmNoticeRepository.save(notice);

        // FCM 발송 (실패해도 트랜잭션 영향 없음)
        fcmSender.send(to.getFcmToken(), req.getPushTitle(), req.getPushBody());

        return AlarmNoticeResponse.of(notice);
    }
}
