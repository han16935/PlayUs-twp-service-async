package com.playus.twpservice.domain.party.listener;

import com.playus.twpservice.domain.common.feign.client.NotificationFeignClient;
import com.playus.twpservice.domain.common.feign.event.PartyNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PartyNotificationEventListener {

    private final NotificationFeignClient notificationFeignClient;

    @Async("notificationAsyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle (PartyNotificationEvent event) {
        notificationFeignClient.notifyParty(event);
    }
}
