package com.playus.twpservice.global.webclient;

import com.playus.twpservice.domain.common.feign.event.PartyNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationServiceClient {

    private final WebClient notificationWebClient;

    public void notifyParty(PartyNotificationEvent event) {
        notificationWebClient.post()
                .uri("/user/api/notifications/party")
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.warn("notification fail", e);
                    return Mono.empty();
                })
                .subscribe();
    }
}
