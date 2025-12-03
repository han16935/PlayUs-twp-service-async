package com.playus.twpservice.domain.party.dto.create;

import com.playus.twpservice.domain.common.validation.MinimumMaximumValidatable;
import com.playus.twpservice.global.validation.ValidEnum;
import com.playus.twpservice.global.validation.ValidEnumList;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.validation.ValidMinimumMaximumParticipants;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;


@Builder
@ValidMinimumMaximumParticipants(message = "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!")
public record PartyCreateRequest (

        @NotBlank(message = "직관팟 제목이 비어 있습니다!")
        @Size(max = 225, message = "제목의 길이를 1~225자 이내로 작성해 주세요!")
        String title,

        @ValidEnum(enumClass = PartyJoinMethod.class, emptyValueMessage = "신청 방식이 비어 있습니다!", invalidValueMessage = "잘못된 신청 방식입니다!")
        String partyJoinMethod,

        @ValidEnum(enumClass = PartyGender.class, emptyValueMessage = "참여 원하는 성별이 비어 있습니다!", invalidValueMessage = "잘못된 성별 형식입니다!")
        String partyGender,

        @ValidEnumList(enumClass = PartyAgeGroup.class, emptyValueMessage = "참여자 나이가 비어 있습니다!",
                       invalidValueMessage = "잘못된 참여자 나이입니다!", overValueMessage = "참여자 나이는 최대 6개까지 가능합니다!")
        List<String> ageGroup,

        @NotNull(message = "최소 참여 인원이 비어 있습니다!")
        @Min(value = 1, message = "최소 참여 인원은 1명 이상이여야 합니다!")
        Long minimumParticipants,

        @NotNull(message = "최대 참여 인원이 비어 있습니다!")
        @Min(value = 1, message = "최대 참여 인원은 1명 이상이여야 합니다!")
        Long maximumParticipants,

        @Size(max = 10, message = "썸네일은 최대 10개까지만 가능합니다!")
        List<
                @NotBlank(message = "사진 파일명이 비어 있습니다!") // List.of() 는 되지만 List.of("") 는 안 됨
                        String>
                thumbnailImageNameList,

        @NotNull(message = "경기 ID는 필수입니다!")
        @Min(value = 1, message = "경기 ID는 1 이상이어야 합니다!")
        Long matchId,

        @NotBlank(message = "직관팟 소개 문구가 비어 있습니다!")
        @Size(max = 100, message = "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!")
        String message

) implements MinimumMaximumValidatable {

    public static PartyCreateRequest of(String title, String method, String gender, List<String> ageGroup,
                                        Long minimumParticipants, Long maximumParticipants,
                                        List<String> thumbnailUrl, Long matchId, String message) {

        return PartyCreateRequest.builder()
                .title(title)
                .partyJoinMethod(method)
                .partyGender(gender)
                .ageGroup(ageGroup)
                .minimumParticipants(minimumParticipants)
                .maximumParticipants(maximumParticipants)
                .thumbnailImageNameList(thumbnailUrl)
                .matchId(matchId)
                .message(message)
                .build();
    }

    public Party toPartyWith(Long userId) {
        return Party.create(title, message, minimumParticipants, maximumParticipants, PartyGender.toEnumValue(partyGender), PartyJoinMethod.toEnumValue(partyJoinMethod), userId, matchId);
    }
}
