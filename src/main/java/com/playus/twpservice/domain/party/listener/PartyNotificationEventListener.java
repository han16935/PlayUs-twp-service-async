package com.playus.twpservice.domain.party.listener;

import com.playus.twpservice.domain.common.feign.event.PartyNotificationEvent;
import com.playus.twpservice.global.webclient.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PartyNotificationEventListener {

    //    private final NotificationFeignClient notificationFeignClient;
    private final NotificationServiceClient notificationWebClient;

//    @Async("notificationAsyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle (PartyNotificationEvent event) {
        notificationWebClient.notifyParty(event);
//        notificationFeignClient.notifyParty(event);
    }
}
