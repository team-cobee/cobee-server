package org.cobee.server.alarm.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmSender {

    public void send(String fcmToken, String title, String body) {
        if (fcmToken == null || fcmToken.isBlank()) {
            return;
        }
        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ALARM_NOT_CREATED);
        }
    }
}
