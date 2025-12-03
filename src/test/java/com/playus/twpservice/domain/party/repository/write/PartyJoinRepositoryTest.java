package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.entity.PartyJoin;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class PartyJoinRepositoryTest extends IntegrationTestSupport {

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    PartyJoinRepository partyJoinRepository;

    @AfterEach
    void tearDown() {
        partyJoinRepository.deleteAll();
        partyRepository.deleteAll();
    }

    @DisplayName("직관팟 ID에 따라 PartyJoin을 삭제할 수 있다.")
    @Test
    void deleteByPartyId() {
        // given
        Long writerId = 1L;
        Long matchId = 1L;
        Long matchId2 = 2L;

        Party party = partyRepository.save(Party.create(
                "title2", "16일 경기 같이 보실 분~", 1L, 15L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId)
        );

        Party otherParty = partyRepository.save(Party.create(
                "title2", "16일 경기 같이 보실 분~", 1L, 15L,
                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, writerId, matchId2)
        );

        partyJoinRepository.saveAll(List.of(
                PartyJoin.create(1L, party, PartyJoinRequestStatus.WAIT, null),
                PartyJoin.create(2L, otherParty, PartyJoinRequestStatus.ACCEPT, "잘 부탁드려요!")
        ));

        // when
        partyJoinRepository.deleteByPartyId(party.getId());

        // then
        assertThat(partyJoinRepository.count()).isEqualTo(1);
    }
}
