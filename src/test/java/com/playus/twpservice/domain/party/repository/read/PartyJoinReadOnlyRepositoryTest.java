package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PartyJoinReadOnlyRepositoryTest extends IntegrationTestSupport {

    @Autowired
    PartyReadOnlyRepository partyReadOnlyRepository;

    @Autowired
    PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;


    @AfterEach
    void tearDown() {
        partyJoinReadOnlyRepository.deleteAll();
        partyReadOnlyRepository.deleteAll();
    }

    @DisplayName("특정 직관팟에 특정 유저가 있는지 확인할 수 있다.")
    @Test
    void findByUserIdAndPartyId() {
        // given
        Long matchId = 1L;
        long userId = 1L;

        PartyDocument p1 = partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(1L, "title1", "text1", 1L, 10L, 2L,
                PartyGender.MALE, PartyJoinMethod.FIRST_COME, 1L, matchId, false));

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, userId, p1.getId(), PartyJoinRequestStatus.ACCEPT, null), // 대상
                PartyJoinDocument.createForOnlyTest(2L, 2L, p1.getId(), PartyJoinRequestStatus.WAIT, null),
                PartyJoinDocument.createForOnlyTest(3L, 3L, p1.getId(), PartyJoinRequestStatus.REFUSE, null)));

        // when
        Optional<PartyJoinDocument> result = partyJoinReadOnlyRepository.findByUserIdAndPartyId(userId, p1.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).extracting("userId", "partyId", "partyJoinRequestStatus")
                .containsExactly(userId, p1.getId(), PartyJoinRequestStatus.ACCEPT);
    }

    @DisplayName("특정 직관팟에 특정 유저가 없을 수 있다..")
    @Test
    void findByUserIdAndPartyId_EMPTY() {
        // given
        Long matchId = 1L;
        long userId = 1L;

        PartyDocument p1 = partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(
                1L, "title1", "text1", 1L, 10L, 2L,
                PartyGender.MALE, PartyJoinMethod.FIRST_COME, 1L, matchId, false)
        );

        // when
        Optional<PartyJoinDocument> result = partyJoinReadOnlyRepository.findByUserIdAndPartyId(userId, p1.getId());

        // then
        assertThat(result).isEmpty();
    }
}
