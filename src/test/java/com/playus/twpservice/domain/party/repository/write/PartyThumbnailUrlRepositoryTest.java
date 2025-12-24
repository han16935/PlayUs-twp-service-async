package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PartyThumbnailUrlRepositoryTest extends IntegrationTestSupport {

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    PartyThumbnailUrlRepository partyThumbnailUrlRepository;

    @AfterEach
    void tearDown() {
        partyThumbnailUrlRepository.deleteAll();
        partyRepository.deleteAll();
    }

    @DisplayName("한 달 이전에 삭제된 PartyThumbnailUrl만 조회된다.")
    @Test
    void findAllDeletedPartyThumbnailUrlBefore() {
        // given
        Long writerId = 1L;
        Long matchId = 1L;

        Party party = partyRepository.save(Party.create(
                "title2", "16일 경기 같이 보실 분~", 1L, 15L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
        );

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime overOneMonthAgo = now.minusMonths(2); // 조건 만족
        LocalDateTime withinOneMonth = now.minusDays(10);   // 조건 불만족

        partyThumbnailUrlRepository.saveAll(List.of(
                PartyThumbnailUrl.create(party, "thumb1").setDeletedAtForOnlyTest(overOneMonthAgo),  // ✅ 포함됨
                PartyThumbnailUrl.create(party, "thumb2").setDeletedAtForOnlyTest(withinOneMonth),   // ❌ 제외됨
                PartyThumbnailUrl.create(party, "thumb3")                                            // ❌ 제외됨
        ));

        // when
        List<PartyThumbnailUrl> result = partyThumbnailUrlRepository.findAllDeletedPartyThumbnailUrlBefore(now.minusMonths(1));

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getThumbnailUrl()).isEqualTo("thumb1");
        assertThat(result.get(0).getDeletedAt()).isNotNull();
    }

}
