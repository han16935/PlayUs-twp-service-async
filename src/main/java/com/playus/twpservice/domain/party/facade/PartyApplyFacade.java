package com.playus.twpservice.domain.party.facade;

import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PartyApplyFacade {

    private final RedissonClient redissonClient;
    private final PartyService partyService;

    public void applyParty(CustomOAuth2User oauth2User, Long partyId) {
        RLock lock = redissonClient.getLock(partyId.toString());
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(10, TimeUnit.SECONDS);
            if (isLocked) {
                partyService.applyPartyFCFS(oauth2User, partyId);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
