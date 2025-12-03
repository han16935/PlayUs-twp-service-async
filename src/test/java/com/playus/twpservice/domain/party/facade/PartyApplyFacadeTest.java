package com.playus.twpservice.domain.party.facade;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.common.security.Gender;
import com.playus.twpservice.domain.common.security.Role;
import com.playus.twpservice.domain.common.security.UserDto;
import com.playus.twpservice.domain.party.document.PartyAgeDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.common.feign.client.NotificationFeignClient;
import com.playus.twpservice.domain.party.repository.read.PartyAgeReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyJoinRepository;
import com.playus.twpservice.domain.party.repository.write.PartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class PartyApplyFacadeTest extends IntegrationTestSupport {

    @Autowired
    PartyApplyFacade partyApplyFacade;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyJoinRepository partyJoinRepository;

    @Autowired
    private PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;

    @Autowired
    private PartyAgeReadOnlyRepository partyAgeReadOnlyRepository;


    @MockitoBean
    private NotificationFeignClient notificationFeignClient;

    @AfterEach
    void tearDown() {
        partyJoinRepository.deleteAll();
        partyRepository.deleteAll();


        partyAgeReadOnlyRepository.deleteAll();
        partyJoinReadOnlyRepository.deleteAll();
    }

    @DisplayName("동시에 직관팟을 신청할 수 있다.")
    @Test
    void applyPart() throws InterruptedException {
        // given
        Long userId = 5L;
        UserDto userDto = UserDto.createForTest(userId, "test", Gender.FEMALE, Role.USER, "http://test.test", 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto, "accessToken");
        Long writerId = 1L;
        Long matchId = 1L;

        Long maximumParticipants = 10L;

        Party party = partyRepository.save(Party.create(
                "title", "설명", 1L, maximumParticipants,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId
        ));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, 2L, party.getId(), PartyJoinRequestStatus.ACCEPT, null),
                PartyJoinDocument.createForOnlyTest(2L, 3L, party.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 4L, party.getId(), PartyJoinRequestStatus.REFUSE, null)
        ));

        partyAgeReadOnlyRepository.saveAll(List.of(
                PartyAgeDocument.createForOnlyTest(1L, party.getId(), 10),
                PartyAgeDocument.createForOnlyTest(2L, party.getId(), 20)
        ));

        int threadCount = 100; // 총 100개의 thread 사용될 예정
        ExecutorService executorService = Executors.newFixedThreadPool(32); // 최대 32개의 thread가 동시 실행
        CountDownLatch latch = new CountDownLatch(threadCount); // 타 스레드 작업 완료될 때까지 대기중

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    partyApplyFacade.applyParty(customOAuth2User, party.getId());
                } finally {
                    latch.countDown(); // 위 method 끝나면 countDown() 통해 latch 카운트 감소
                }
            });
        }

        latch.await(); // 모든 작업 끝날 때까지 기다림, count = 0이 되면 아래 진행

        // then
        Party afterParty = partyRepository.findAll().get(0);
        assertThat(afterParty.getCurrentParticipants()).isEqualTo(afterParty.getMaximumParticipants());
        assertThat(partyJoinRepository.count()).isEqualTo(maximumParticipants - 1); // 직관팟 작성자는 PartyJoin에 들어가지 않음
    }
}
