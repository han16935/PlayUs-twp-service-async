package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.domain.party.entity.PartyAge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartyAgeRepository extends JpaRepository<PartyAge, Long> {

    @Modifying
    @Query("UPDATE PartyAge p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.party.id = :partyId")
    void deleteByPartyId(@Param("partyId") Long partyId);

    List<PartyAge> findByPartyId(Long partyId);
}
