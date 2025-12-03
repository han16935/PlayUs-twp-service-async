package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.domain.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {
//    Optional<Party> findByChatRoomId(Long chatRoomId);
}
