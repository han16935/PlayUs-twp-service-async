package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.domain.party.entity.PartyJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PartyJoinRepository extends JpaRepository<PartyJoin, Long> {

    @Modifying
    @Query("UPDATE PartyJoin p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.party.id = :partyId")
    void deleteByPartyId(@Param("partyId") Long partyId);

    @Query("SELECT p FROM PartyJoin p WHERE p.party.id = :partyId AND p.userId = :userId")
    Optional<PartyJoin> findByPartyIdAndUserId(@Param("partyId") Long partyId, @Param("userId") Long userId);
}
