package com.playus.twpservice.domain.party.document;

import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyGender;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(value = "party")
public class PartyDocument {

    @Id
    private Long id;

    @NotNull
    @Size(min = 1, max = 225)
    private String title;

    @NotNull
    @Field(name = "party_join_method")
    private PartyJoinMethod partyJoinMethod;

    @NotNull
    @Field(name = "party_gender")
    private PartyGender partyGender;

    @NotNull
    @Field(name = "minimum_participants")
    private Long minimumParticipants;

    @NotNull
    @Field(name = "maximum_participants")
    private Long maximumParticipants;

    @NotNull
    @Field(name = "current_participants")
    private Long currentParticipants;

    @NotNull
    @Field(name = "writer_id")
    private Long writerId;

    @NotNull
    @Indexed
    @Field(name = "match_id")
    private Long matchId;


    @NotNull
    private String text;

    @NotNull
    @Field(name = "is_ended")
    private Boolean isEnded;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    private PartyDocument(Long id, String title, String text, Long minimumParticipants, Long maximumParticipants, Long currentParticipants,
                          PartyGender partyGender, PartyJoinMethod partyJoinMethod, Long writerId, Long matchId, Boolean isEnded) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.minimumParticipants = minimumParticipants;
        this.maximumParticipants = maximumParticipants;
        this.currentParticipants = currentParticipants;
        this.partyGender = partyGender;
        this.partyJoinMethod = partyJoinMethod;
        this.matchId = matchId;
        this.isEnded = isEnded;
        this.writerId = writerId;
    }

    public static PartyDocument createForOnlyTest(Long id, String title, String text, Long minimumParticipants,
                                                  Long maximumParticipants, Long currentParticipantsCount, PartyGender partyGender, PartyJoinMethod partyJoinMethod,
                                                  Long writerId, Long matchId, Boolean isEnded) {
        return PartyDocument.builder()
                .id(id)
                .title(title)
                .text(text)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .currentParticipants(currentParticipantsCount)
                .partyGender(partyGender)
                .partyJoinMethod(partyJoinMethod)
                .writerId(writerId)
                .matchId(matchId)
                .isEnded(isEnded)
                .build();
    }
}
