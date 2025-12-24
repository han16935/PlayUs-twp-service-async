package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.IntegrationTestSupport;
import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.common.security.Gender;
import com.playus.twpservice.domain.common.security.Role;
import com.playus.twpservice.domain.common.security.UserDto;
import com.playus.twpservice.domain.party.document.PartyAgeDocument;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import com.playus.twpservice.domain.party.document.PartyThumbnailUrlDocument;
import com.playus.twpservice.domain.party.dto.applieduser.PartyAppliedUserResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.participants.PartyParticipantsInfoResponse;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.exception.document.PartyDocumentException;
import com.playus.twpservice.domain.party.exception.entity.PartyException;
import com.playus.twpservice.domain.common.feign.client.UserFeignClient;
import com.playus.twpservice.domain.common.feign.response.PartyParticipantsInfoFeignResponse;
import com.playus.twpservice.domain.party.repository.read.PartyAgeReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyThumbnailUrlReadOnlyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * 직관팟 리스트/상세 정보 불러오는 기능은 TestContainers에서 저장햇을 때 만들어지는 collection 구조와
 * CDC 통해 만들어지는 Collection 구조가 달라 주석 처리 << 관련해서 5/22 오전 프론트에서 테스트햇을 때 성공함
 */
class PartyReadOnlyServiceTest extends IntegrationTestSupport {

    @Autowired
    PartyReadOnlyService partyReadOnlyService;

    @Autowired
    PartyReadOnlyRepository partyReadOnlyRepository;

    @Autowired
    PartyAgeReadOnlyRepository partyAgeReadOnlyRepository;

    @Autowired
    PartyThumbnailUrlReadOnlyRepository partyThumbnailUrlReadOnlyRepository;

    @Autowired
    PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;

    @MockitoBean
    protected UserFeignClient userFeignClient;

    @AfterEach
    void tearDown() {
        partyReadOnlyRepository.deleteAll();
        partyAgeReadOnlyRepository.deleteAll();
        partyThumbnailUrlReadOnlyRepository.deleteAll();
        partyJoinReadOnlyRepository.deleteAll();
    }

//    @DisplayName("특정 경기에 대한 직관팟을 불러올 수 있다.")
//    @Test
//    void getPartyInfoListByMatchId() {
//        // given
//        Long matchId = 1L;
//
//        given(userFeignClient.getPartyUserThumbnailUrls(List.of())).willReturn(PartyUserThumbnailUrlListResponse.of(new ArrayList<>()));
//        given(userFeignClient.getPartyUserThumbnailUrls(List.of(4L, 5L))).willReturn(
//                PartyUserThumbnailUrlListResponse.of(new ArrayList<>(List.of("http://user1", "http://user2")))
//        );
//
//        given(userFeignClient.getWriterInfo(List.of(1L, 2L))).willReturn(List.of(
//                PartyWriterInfoFeignResponse.of(1L, "writer1", "남성", 17, "http://writer1-thumbnail"),
//                PartyWriterInfoFeignResponse.of(2L, "writer2", "여성", 26, "http://writer2-thumbnail")
//        ));
//
//        PartyDocument p1 = PartyDocument.createForOnlyTest(1L, "title1", "text1", 1L, 10L, 3L,
//                PartyGender.MALE, PartyJoinMethod.FIRST_COME, 1L, matchId, false, 1L); // 대상
//        PartyDocument p2 = PartyDocument.createForOnlyTest(2L, "title2", "text2", 1L, 10L, 1L,
//                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, 2L, matchId, false, 2L); // 대상
//        PartyDocument p3 = PartyDocument.createForOnlyTest(3L, "title3", "text3", 1L, 10L, 1L,
//                PartyGender.NO_MATTER, PartyJoinMethod.RESERVATION, 3L, matchId + 1, false, 3L);
//        PartyDocument p4 = PartyDocument.createForOnlyTest(4L, "title4", "text4", 1L, 10L, 1L,
//                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, 4L, matchId, true, 4L);
//
//        List<PartyDocument> partyDocuments = partyReadOnlyRepository.saveAll(List.of(p1, p2, p3, p4));
//
//        PartyAgeDocument pa1 = PartyAgeDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), 10);
//        PartyAgeDocument pa2 = PartyAgeDocument.createForOnlyTest(2L, partyDocuments.get(1).getId(), 20);
//        PartyAgeDocument pa3 = PartyAgeDocument.createForOnlyTest(3L, partyDocuments.get(3).getId(), 20);
//        partyAgeReadOnlyRepository.saveAll(List.of(pa1, pa2, pa3));
//
//        PartyThumbnailUrlDocument ptu1 = PartyThumbnailUrlDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), "thumbnailUrl1");
//        PartyThumbnailUrlDocument ptu2 = PartyThumbnailUrlDocument.createForOnlyTest(2L, partyDocuments.get(0).getId(), "thumbnailUrl2");
//        partyThumbnailUrlReadOnlyRepository.saveAll(List.of(ptu1, ptu2));
//
//        PartyJoinDocument pj1 = PartyJoinDocument.createForOnlyTest(1L, 4L, partyDocuments.get(0).getId(), PartyJoinRequestStatus.ACCEPT, null);
//        PartyJoinDocument pj2 = PartyJoinDocument.createForOnlyTest(2L, 5L, partyDocuments.get(0).getId(), PartyJoinRequestStatus.WAIT, "가입 원합니다!");
//        partyJoinReadOnlyRepository.saveAll(List.of(pj1, pj2));
//
//        // when
//        List<PartyInfoResponse> result = partyReadOnlyService.getPartyInfoListByMatchId(matchId);
//
//        // then
//        assertThat(result).hasSize(2)
//
//                .extracting("partyId", "writerId", "title", "partyJoinMethod", "partyAges", "availableGender", "authorName", "authorGender", "authorAge",
//                         "currentParticipantsCount", "maximumParticipantsCount", "partyThumbnailUrls", "userThumbnailUrls")
//
//                .containsExactlyInAnyOrder(
//                        tuple(1L, 1L, "title1", PartyJoinMethod.FIRST_COME.getDescription(), List.of("10대"), PartyGender.MALE.getDescription(), "writer1", "남성", "10대",
//                                3L, 10L, List.of("thumbnailUrl1", "thumbnailUrl2"), List.of("http://writer1-thumbnail", "http://user1", "http://user2")),
//
//                        tuple(2L, 2L, "title2", PartyJoinMethod.RESERVATION.getDescription(), List.of("20대"), PartyGender.FEMALE.getDescription(), "writer2", "여성", "20대",
//                                1L, 10L, List.of(), List.of("http://writer2-thumbnail"))
//                );
//    }

    @DisplayName("특정 경기에 대한 직관팟이 없을 수 있다.")
    @Test
    void getPartyInfoList_EMPTY_PARTYByMatchId() {

        // given
        Long matchId = 1L;

        // when
        List<PartyInfoResponse> result = partyReadOnlyService.getPartyInfoListByMatchId(matchId);

        // then
        assertThat(result).isEmpty();
    }

//    @DisplayName("직관팟의 자세한 정보를 가져올 수 있다.")
//    @Test
//    void getPartyDetail() {
//
//        Long partyId = 1L;
//        Long writerId = 1L;
//        Long matchId = 1L;
//
//        given(userFeignClient.getPartyUserThumbnailUrls(List.of())).willReturn(PartyUserThumbnailUrlListResponse.of(new ArrayList<>()));
//        given(userFeignClient.getPartyUserThumbnailUrls(List.of(4L, 5L))).willReturn(
//                PartyUserThumbnailUrlListResponse.of(new ArrayList<>(List.of("http://user1", "http://user2")))
//        );
//
//        given(userFeignClient.getWriterInfo(List.of(writerId))).willReturn(List.of(
//                PartyWriterInfoFeignResponse.of(1L, "writer1", "남성", 35, "http://writer1-thumbnail")
//        ));
//
//        LocalDateTime matchDate = LocalDateTime.of(2025, 3, 22, 14, 0);
//        given(matchFeignClient.getMatchDate(matchId)).willReturn(matchDate);
//
//        PartyDocument p1 = PartyDocument.createForOnlyTest(partyId, "title1", "text1", 1L, 10L, 3L,
//                PartyGender.MALE, PartyJoinMethod.FIRST_COME, writerId, matchId, "chatRoomId"); // 대상
//        PartyDocument p2 = PartyDocument.createForOnlyTest(2L, "title2", "text2", 1L, 10L, 1L,
//                PartyGender.FEMALE, PartyJoinMethod.RESERVATION, 2L, matchId, "chatRoom2Id"); // 대상
//        PartyDocument p3 = PartyDocument.createForOnlyTest(3L, "title3", "text3", 1L, 10L, 1L,
//                PartyGender.NO_MATTER, PartyJoinMethod.RESERVATION, 3L, matchId + 1, "chatRoom3Id");
//
//        List<PartyDocument> partyDocuments = partyReadOnlyRepository.saveAll(List.of(p1, p2, p3));
//
//        PartyAgeDocument pa1 = PartyAgeDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), 10);
//        PartyAgeDocument pa2 = PartyAgeDocument.createForOnlyTest(2L, partyDocuments.get(1).getId(), 20);
//        partyAgeReadOnlyRepository.saveAll(List.of(pa1, pa2));
//
//        PartyThumbnailUrlDocument ptu1 = PartyThumbnailUrlDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), "thumbnailUrl1");
//        PartyThumbnailUrlDocument ptu2 = PartyThumbnailUrlDocument.createForOnlyTest(2L, partyDocuments.get(0).getId(), "thumbnailUrl2");
//        partyThumbnailUrlReadOnlyRepository.saveAll(List.of(ptu1, ptu2));
//
//        PartyJoinDocument pj1 = PartyJoinDocument.createForOnlyTest(1L, 4L, partyDocuments.get(0).getId(), PartyJoinRequestStatus.ACCEPT, null);
//        PartyJoinDocument pj2 = PartyJoinDocument.createForOnlyTest(2L, 5L, partyDocuments.get(0).getId(), PartyJoinRequestStatus.WAIT, "가입 원합니다!");
//        partyJoinReadOnlyRepository.saveAll(List.of(pj1, pj2));
//
//        // when
//        PartyDetailResponse result = partyReadOnlyService.getPartyDetail(partyId);
//
//        // then
//        assertThat(result)
//
//                .extracting("partyId", "writerId", "title", "partyJoinMethod", "text", "partyAges", "availableGender", "authorName", "authorGender", "authorAge",
//                        "matchDate", "currentParticipantsCount", "maximumParticipantsCount", "partyThumbnailUrls", "userThumbnailUrls")
//
//                .containsExactly(
//                        1L, 1L, "title1", PartyJoinMethod.FIRST_COME.getDescription(), "text1", List.of("10대"), PartyGender.MALE.getDescription(), "writer1", "남성", "30대",
//                        matchDate, 3L, 10L, List.of("thumbnailUrl1", "thumbnailUrl2"), List.of("http://writer1-thumbnail", "http://user1", "http://user2")
//
//                );
//    }

    @DisplayName("직관팟 상세정보 조회 시, 직관팟이 존재하지 않을 수 있다.")
    @Test
    void getPartyDetail_NOT_FOUND() {

        Long partyId = 1L;

        // when // then
        assertThatThrownBy(() -> partyReadOnlyService.getPartyDetail(partyId))
                .isInstanceOf(PartyDocumentException.NotFoundException.class)
                .hasMessage("직관팟이 존재하지 않습니다!");
    }

    @DisplayName("직관팟에 신청한 유저의 정보를 불러올 수 있다.")
    @Test
    void getAppliedUsers() {
        // given
        long userId = 1L;
        Long matchId = 1L;

        given(userFeignClient.getPartyApplicantsInfo(List.of(userId + 1, userId + 2))).willReturn(
                List.of(
                        PartyParticipantsInfoFeignResponse.of(userId + 1, "kim", 14, "http://user1.jpg"),
                        PartyParticipantsInfoFeignResponse.of(userId + 2, "jung", 27, "http://user2.jpg")
                )
        );

        PartyDocument p1 = partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(
                1L, "title1", "text1", 1L, 10L, 3L,
                PartyGender.MALE, PartyJoinMethod.FIRST_COME, userId, matchId, false)
        );

        partyJoinReadOnlyRepository.saveAll(
                List.of(
                        PartyJoinDocument.createForOnlyTest(1L, userId + 1, p1.getId(), PartyJoinRequestStatus.WAIT, "참여 희망"),
                        PartyJoinDocument.createForOnlyTest(2L, userId + 2, p1.getId(), PartyJoinRequestStatus.WAIT, "참여 희망2"),
                        PartyJoinDocument.createForOnlyTest(3L, userId + 3, p1.getId(), PartyJoinRequestStatus.REFUSE, "참여 희망3")
                )
        );

        // when
        List<PartyAppliedUserResponse> response = partyReadOnlyService.getAppliedUsers(userId, p1.getId());

        // then
        assertThat(response).hasSize(2)
                .extracting("userId", "name", "ageGroup", "thumbnailUrl", "requireMessage")
                .containsExactlyInAnyOrder(
                        tuple(userId + 1, "kim", "10대", "http://user1.jpg", "참여 희망"),
                        tuple(userId + 2, "jung", "20대", "http://user2.jpg", "참여 희망2")
                );
    }

    @DisplayName("잘못된 직관팟에 대해서 신청 유저 목록을 조회할 수 없다.")
    @Test
    void getAppliedUsers_INVALID_PARTY() {
        // given
        long userId = 1L;
        Long matchId = 1L;
        given(userFeignClient.getPartyApplicantsInfo(List.of(userId + 1, userId + 2))).willReturn(
                List.of(
                        PartyParticipantsInfoFeignResponse.of(userId + 1, "kim", 14, "http://user1.jpg"),
                        PartyParticipantsInfoFeignResponse.of(userId + 2, "jung", 27, "http://user2.jpg")
                )
        );

        PartyDocument p1 = partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(
                1L, "title1", "text1", 1L, 10L, 3L,
                PartyGender.MALE, PartyJoinMethod.FIRST_COME, userId, matchId, false)
        );

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, userId + 1, p1.getId(), PartyJoinRequestStatus.WAIT, "참여 희망"),
                PartyJoinDocument.createForOnlyTest(2L, userId + 2, p1.getId(), PartyJoinRequestStatus.WAIT, "참여 희망2"),
                PartyJoinDocument.createForOnlyTest(3L, userId + 3, p1.getId(), PartyJoinRequestStatus.REFUSE, "참여 희망3")
        ));

        // when // then
        assertThatThrownBy(() -> partyReadOnlyService.getAppliedUsers(userId, p1.getId() + 1))
                .isInstanceOf(PartyDocumentException.NotFoundException.class)
                .hasMessage("직관팟이 존재하지 않습니다!");
    }

    @DisplayName("직관팟 방장이 아니면 신청 유저 목록을 가져올 수 없다.")
    @Test
    void getAppliedUsers_NOT_WRITER() {
        long userId = 1L;
        Long matchId = 1L;

        given(userFeignClient.getPartyApplicantsInfo(List.of(userId + 1, userId + 2))).willReturn(
                List.of(
                        PartyParticipantsInfoFeignResponse.of(userId + 1, "kim", 14, "http://user1.jpg"),
                        PartyParticipantsInfoFeignResponse.of(userId + 2, "jung", 27, "http://user2.jpg")
                )
        );

        PartyDocument p1 = partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(
                1L, "title1", "text1", 1L, 10L, 3L,
                PartyGender.MALE, PartyJoinMethod.FIRST_COME, userId + 1, matchId, false)
        );

        partyJoinReadOnlyRepository.saveAll(List.of(
                PartyJoinDocument.createForOnlyTest(1L, userId + 1, p1.getId(), PartyJoinRequestStatus.WAIT, "참여 희망"),
                PartyJoinDocument.createForOnlyTest(2L, userId + 2, p1.getId(), PartyJoinRequestStatus.WAIT, "참여 희망2"),
                PartyJoinDocument.createForOnlyTest(3L, userId + 3, p1.getId(), PartyJoinRequestStatus.REFUSE, "참여 희망3")
        ));

        // when // then
        assertThatThrownBy(() -> partyReadOnlyService.getAppliedUsers(userId, p1.getId()))
                .isInstanceOf(PartyException.NotPartyWriterException.class)
                .hasMessage("방장이 아니면 직관팟 지원자를 조회할 수 없습니다!");
    }

    @DisplayName("직관팟에 신청한 유저가 없을 수 있다.")
    @Test
    void getAppliedUsers_EMPTY_APPLICANTS() {
        // given
        long userId = 1L;
        Long matchId = 1L;
        given(userFeignClient.getPartyApplicantsInfo(List.of(userId + 1, userId + 2))).willReturn(
                List.of()
        );

        PartyDocument p1 = partyReadOnlyRepository.save(PartyDocument.createForOnlyTest(
                1L, "title1", "text1", 1L, 10L, 3L,
                PartyGender.MALE, PartyJoinMethod.FIRST_COME, userId, matchId, false)
        );

        // when
        List<PartyAppliedUserResponse> response = partyReadOnlyService.getAppliedUsers(userId, p1.getId());

        // then
        assertThat(response).isEmpty();
    }

//    @DisplayName("자신이 지원한 직관팟 정보를 가져올 수 있다.")
//    @Test
//    void getAppliedParties() {
//
//        // given
//        Long userId = 1L;
//        Long matchId = 1L;
//
//        UserDto userDto = UserDto.createForTest(userId, Gender.FEMALE, Role.USER, 20);
//        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
//
//        given(userFeignClient.getWriterInfo(List.of(userId + 1, userId + 2))).willReturn(List.of(
//                PartyWriterInfoFeignResponse.of(userId + 1, "writer1", "남성", 17, "http://writer1-thumbnail"),
//                PartyWriterInfoFeignResponse.of(userId + 2, "writer2", "여성", 26, "http://writer2-thumbnail")
//        ));
//
//        List<PartyDocument> partyDocuments = partyReadOnlyRepository.saveAll(List.of(
//                PartyDocument.createForOnlyTest(1L, "title1", "text1", 1L, 10L, 1L,
//                        PartyGender.MALE, PartyJoinMethod.FIRST_COME, userId + 1, matchId, "chatRoomId"), // 대상
//
//                PartyDocument.createForOnlyTest(2L, "title2", "text2", 1L, 10L, 1L,
//                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, userId + 2, matchId + 1, "chatRoom2Id"), // 대상
//
//                PartyDocument.createForOnlyTest(3L, "title3", "text3", 1L, 10L, 1L,
//                        PartyGender.NO_MATTER, PartyJoinMethod.RESERVATION, userId + 3, matchId + 2, "chatRoom3Id")
//        ));
//
//        partyAgeReadOnlyRepository.saveAll(List.of(
//                PartyAgeDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), 10), // 대상
//                PartyAgeDocument.createForOnlyTest(2L, partyDocuments.get(0).getId(), 20), // 대상
//                PartyAgeDocument.createForOnlyTest(3L, partyDocuments.get(1).getId(), 30), // 대상
//                PartyAgeDocument.createForOnlyTest(4L, partyDocuments.get(1).getId(), 40), // 대상
//                PartyAgeDocument.createForOnlyTest(5L, partyDocuments.get(2).getId(), 50)
//        ));
//
//        partyJoinReadOnlyRepository.saveAll(List.of(
//                PartyJoinDocument.createForOnlyTest(1L, userId, partyDocuments.get(0).getId(), PartyJoinRequestStatus.ACCEPT, null), // 대상
//                PartyJoinDocument.createForOnlyTest(2L, userId, partyDocuments.get(1).getId(), PartyJoinRequestStatus.WAIT, "가입 원합니다!"), // 대상
//                PartyJoinDocument.createForOnlyTest(3L, userId + 1, partyDocuments.get(2).getId(), PartyJoinRequestStatus.WAIT, "가입 원합니다!")
//        ));
//
//        partyThumbnailUrlReadOnlyRepository.saveAll(List.of(
//                PartyThumbnailUrlDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), "thumbnailUrl1"),
//                PartyThumbnailUrlDocument.createForOnlyTest(2L, partyDocuments.get(0).getId(), "thumbnailUrl2")
//        ));
//
//        // when
//        List<AppliedPartyResponse> result = partyReadOnlyService.getAppliedParties(customOAuth2User);
//
//        // then
//        assertThat(result).hasSize(2)
//                .extracting("partyId", "title", "partyAgeGroup", "partyGender", "partyJoinRequestStatus",
//                        "writerId", "authorName", "authorGender", "authorAge", "writerThumbnailUrl", "currentParticipants")
//                .containsExactlyInAnyOrder(
//                        tuple(1L, "title1", List.of("10대", "20대"), "남자만", "채팅방 입장!", userId + 1, "writer1", "남성", "10대", "http://writer1-thumbnail", 1L),
//                        tuple(2L, "title2", List.of("30대", "40대"), "여자만", "신청중", userId + 2, "writer2", "여성", "20대", "http://writer2-thumbnail", 1L)
//                );
//    }

    @DisplayName("직관팟 참가자를 조회할 수 있다.")
    @Test
    void getParticipants() {
        // given
        Long userId = 1L;
        Long matchId = 1L;

        UserDto userDto = UserDto.createForTest(userId, "nickname", Gender.FEMALE, Role.USER, "http://image.com", 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto, "accesstoken");

        List<PartyDocument> partyDocuments = partyReadOnlyRepository.saveAll(List.of(
                PartyDocument.createForOnlyTest(1L, "title1", "text1", 1L, 10L, 1L,
                        PartyGender.MALE, PartyJoinMethod.FIRST_COME, userId, matchId, false), // 대상

                PartyDocument.createForOnlyTest(2L, "title2", "text2", 1L, 10L, 1L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, userId + 2, matchId + 1, false)
        ));

        partyAgeReadOnlyRepository.saveAll(List.of(
                PartyAgeDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), 10),
                PartyAgeDocument.createForOnlyTest(2L, partyDocuments.get(0).getId(), 20),
                PartyAgeDocument.createForOnlyTest(3L, partyDocuments.get(1).getId(), 30),
                PartyAgeDocument.createForOnlyTest(4L, partyDocuments.get(1).getId(), 40)
        ));

        partyJoinReadOnlyRepository.saveAll(List.of(

                // 대상
                PartyJoinDocument.createForOnlyTest(1L, userId + 1, partyDocuments.get(0).getId(), PartyJoinRequestStatus.ACCEPT, null),

                // 대상
                PartyJoinDocument.createForOnlyTest(2L, userId + 2, partyDocuments.get(0).getId(), PartyJoinRequestStatus.ACCEPT, "가입 원합니다!"),

                PartyJoinDocument.createForOnlyTest(3L, userId + 3, partyDocuments.get(0).getId(), PartyJoinRequestStatus.WAIT, "가입 원합니다!")
        ));

        given(userFeignClient.getPartyApplicantsInfo(List.of(userId + 1, userId + 2))).willReturn(List.of(
                PartyParticipantsInfoFeignResponse.of(userId + 2, "writer2", 26, "http://writer2-thumbnail"),
                PartyParticipantsInfoFeignResponse.of(userId + 1, "writer1",   17,"http://writer1-thumbnail")
        ));

        partyThumbnailUrlReadOnlyRepository.saveAll(List.of(
                PartyThumbnailUrlDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), "thumbnailUrl1"),
                PartyThumbnailUrlDocument.createForOnlyTest(2L, partyDocuments.get(0).getId(), "thumbnailUrl2")
        ));

        // when
        List<PartyParticipantsInfoResponse> result = partyReadOnlyService.getParticipants(customOAuth2User, partyDocuments.get(0).getId());

        // then
        assertThat(result).hasSize(2)
                .extracting("userId", "name")
                .containsExactlyInAnyOrder(
                        tuple(userId + 1, "writer1"),
                        tuple(userId + 2, "writer2")
                );
    }

    @DisplayName("존재하지 않는 직관팟에 대한 참가자를 조회할 수 없다")
    @Test
    void getParticipants_INVALID_PARTY() {
        // given
        Long userId = 1L;

        UserDto userDto = UserDto.createForTest(userId, "nickname", Gender.FEMALE, Role.USER, "http://image.com", 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto, "accesstoken");

        // when // then
        assertThatThrownBy(() -> partyReadOnlyService.getParticipants(customOAuth2User, 1L))
                .isInstanceOf(PartyDocumentException.NotFoundException.class)
                .hasMessage("직관팟이 존재하지 않습니다!");
    }

    @DisplayName("직관팟 참가자를 조회할 때, 본인은 반드시 그 직관팟에 참여되어 있어야 한다.")
    @Test
    void getParticipants_MUST_PARTICIPATE() {
        // given
        Long userId = 1L;
        Long matchId = 1L;

        UserDto userDto = UserDto.createForTest(userId + 1000, "nickname", Gender.FEMALE, Role.USER, "http://image.com", 20);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto, "accesstoken");

        List<PartyDocument> partyDocuments = partyReadOnlyRepository.saveAll(List.of(
                PartyDocument.createForOnlyTest(1L, "title1", "text1", 1L, 10L, 1L,
                        PartyGender.MALE, PartyJoinMethod.FIRST_COME, userId, matchId, false), // 대상

                PartyDocument.createForOnlyTest(2L, "title2", "text2", 1L, 10L, 1L,
                        PartyGender.FEMALE, PartyJoinMethod.RESERVATION, userId + 2, matchId + 1, false)
        ));

        partyAgeReadOnlyRepository.saveAll(List.of(
                PartyAgeDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), 10),
                PartyAgeDocument.createForOnlyTest(2L, partyDocuments.get(0).getId(), 20),
                PartyAgeDocument.createForOnlyTest(3L, partyDocuments.get(1).getId(), 30),
                PartyAgeDocument.createForOnlyTest(4L, partyDocuments.get(1).getId(), 40)
        ));

        partyJoinReadOnlyRepository.saveAll(List.of(

                // 대상
                PartyJoinDocument.createForOnlyTest(1L, userId + 1, partyDocuments.get(0).getId(), PartyJoinRequestStatus.ACCEPT, null),

                // 대상
                PartyJoinDocument.createForOnlyTest(2L, userId + 2, partyDocuments.get(0).getId(), PartyJoinRequestStatus.ACCEPT, "가입 원합니다!"),

                PartyJoinDocument.createForOnlyTest(3L, userId + 3, partyDocuments.get(0).getId(), PartyJoinRequestStatus.WAIT, "가입 원합니다!")
        ));

        given(userFeignClient.getPartyApplicantsInfo(List.of(userId + 1, userId + 2))).willReturn(List.of(
                PartyParticipantsInfoFeignResponse.of(userId + 1, "writer1",   17,"http://writer1-thumbnail"),
                PartyParticipantsInfoFeignResponse.of(userId + 2, "writer2", 26, "http://writer2-thumbnail")
        ));

        partyThumbnailUrlReadOnlyRepository.saveAll(List.of(
                PartyThumbnailUrlDocument.createForOnlyTest(1L, partyDocuments.get(0).getId(), "thumbnailUrl1"),
                PartyThumbnailUrlDocument.createForOnlyTest(2L, partyDocuments.get(0).getId(), "thumbnailUrl2")
        ));

        // when // then
        assertThatThrownBy(() -> partyReadOnlyService.getParticipants(customOAuth2User, 1L))
                .isInstanceOf(PartyException.ParticipantsNotFoundException.class)
                .hasMessage("이 직관팟에 참여하지 않은 인원입니다!");
    }
}
