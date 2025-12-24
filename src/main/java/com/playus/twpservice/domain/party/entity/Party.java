package com.playus.twpservice.domain.party.entity;

import com.playus.twpservice.domain.common.data.BaseTimeEntity;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyGender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

import static com.playus.twpservice.domain.party.exception.entity.PartyException.*;


/**
 * 25/5/19 작성
 *    직관팟 생성 시 작성자 관련해서는 PartyJoin에 저장되지 않음 (joinStatus, requireMessage 사실상 고정이여서)
 *
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE party SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "party", indexes = @Index(name = "idx_party_match_id", columnList = "match_id"))
public class Party extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 225)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyJoinMethod partyJoinMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "party_gender")
    private PartyGender partyGender;

    @Column(nullable = false,name = "minimum_participants")
    private Long minimumParticipants;

    @Column(nullable = false, name = "maximum_participants")
    private Long maximumParticipants;

    @Column(nullable = false, name = "current_participants")
    private Long currentParticipants = 1L;

    @Column(nullable = false, name = "writer_id")
    private Long writerId;

    @Column(nullable = false, name = "match_id")
    private Long matchId;

//    @OneToOne
//    @JoinColumn(nullable = false, name = "chat_room_id")
//    private ChatRoom chatRoom;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false, name = "is_ended")
    private Boolean isEnded = false;

    private LocalDateTime deletedAt;

    @Builder
    private Party(Long id, String title, String text, Long minimumParticipants, Long maximumParticipants, PartyGender partyGender, PartyJoinMethod partyJoinMethod, Long writerId, Long matchId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.minimumParticipants = minimumParticipants;
        this.maximumParticipants = maximumParticipants;
        this.partyGender = partyGender;
        this.partyJoinMethod = partyJoinMethod;
        this.writerId = writerId;
        this.matchId = matchId;
    }

    // setter
    public void updateParty(PartyUpdateRequest updateRequest) {
        this.title = updateRequest.title();
        this.text = updateRequest.message();
        this.minimumParticipants = updateRequest.minimumParticipants();
        this.maximumParticipants = updateRequest.maximumParticipants();
        this.partyGender = PartyGender.toEnumValue(updateRequest.partyGender());
        this.partyJoinMethod = PartyJoinMethod.toEnumValue(updateRequest.partyJoinMethod());
    }

    public Party terminateParty() {
        this.isEnded = true;
        return this;
    }

    public void increaseCurrentParticipants() {
        if (this.currentParticipants >= this.maximumParticipants) {
            throw new ExceedPartyParticipantsException("직관팟 정원이 초과되었습니다!");
        }
        this.currentParticipants++;
    }

    public void decreaseCurrentMember() {
        if (this.currentParticipants <= this.minimumParticipants) {
            throw new InsufficientPartyParticipantsException("직관팟 정원이 부족합니다!");
        }
        this.currentParticipants--;
    }

    public Party setCurrentParticipantsForOnlyTest(Long currentParticipants) {
        this.currentParticipants = currentParticipants;
        return this;
    }

    public static Party create(String title, String text, Long minimumParticipants, Long maximumParticipants,
                               PartyGender partyGender, PartyJoinMethod partyJoinMethod, Long writerId, Long matchId) {
        return Party.builder()
                .title(title)
                .text(text)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .partyGender(partyGender)
                .partyJoinMethod(partyJoinMethod)
                .writerId(writerId)
                .matchId(matchId)
                .build();
    }

    public Boolean isEndedParty() {
        return this.isEnded;
    }
}
