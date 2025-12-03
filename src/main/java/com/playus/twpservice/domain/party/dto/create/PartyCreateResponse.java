package com.playus.twpservice.domain.party.dto.create;

import lombok.Builder;

@Builder
public record PartyCreateResponse (
      Long partyId
) {

    public static PartyCreateResponse of(Long partyId) {
        return PartyCreateResponse.builder()
                .partyId(partyId)
                .build();
    }
}
