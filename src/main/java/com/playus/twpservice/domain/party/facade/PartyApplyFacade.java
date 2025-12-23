package com.playus.twpservice.domain.party.facade;

import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.party.service.PartyService;
import com.playus.twpservice.global.exception.LockAcquireFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class PartyApplyFacade {

    private final RedissonClient redissonClient;
    private final PartyService partyService;

    public void applyParty(CustomOAuth2User oauth2User, Long partyId) {
        RLock lock = redissonClient.getLock(partyId.toString());
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(0, TimeUnit.MILLISECONDS);
            if (isLocked) {
                partyService.applyPartyFCFS(oauth2User, partyId);
            }

            else {
                throw new LockAcquireFailException("서버가 혼잡합니다. 잠시 후 시도해주세요!");
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
