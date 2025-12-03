package com.playus.twpservice.domain.party.controller;

import com.playus.twpservice.ControllerTestSupport;
import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.common.security.Role;
import com.playus.twpservice.domain.party.dto.appliedparty.AppliedPartyResponse;
import com.playus.twpservice.domain.party.dto.applieduser.PartyAppliedUserResponse;
import com.playus.twpservice.domain.party.dto.apply.PartyApplyResponse;
import com.playus.twpservice.domain.party.dto.apply.PartyApproveApplyRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveResponse;
import com.playus.twpservice.domain.party.dto.cancel.PartyCancelResponse;
import com.playus.twpservice.domain.party.dto.delete.PartyDeleteResponse;
import com.playus.twpservice.domain.party.dto.end.PartyEndResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.create.PartyCreateResponse;
import com.playus.twpservice.domain.common.request.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.leave.PartyLeaveResponse;
import com.playus.twpservice.domain.party.dto.participants.PartyParticipantsInfoResponse;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyGender;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PartyControllerTest extends ControllerTestSupport {

    //    Long userId = 1L;
    List<String> thumbnailUrl = List.of("http://image.com");
//    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(applicantUserId, null, List.of(new SimpleGrantedAuthority("USER")));

    private UsernamePasswordAuthenticationToken token;

    @BeforeEach
    void setUp() {
        long userId = 1L;

        // 더미 OAuth2 사용자
        CustomOAuth2User principal = Mockito.mock(CustomOAuth2User.class);
        when(principal.getName()).thenReturn(Long.toString(userId));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.USER.name()));
        doReturn(authorities).when(principal).getAuthorities();

        token = new UsernamePasswordAuthenticationToken(
                principal, null, authorities
        );
    }

    //  직관팟 생성 happy case
    @DisplayName("직관팟을 생성할 수 있다.")
    @Test
    void createParty() throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(post("/party")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partyId").value("1"));
    }

    //  직관팟 생성 title 이슈
    @DisplayName("직관팟 생성 중 제목은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "title = {0}")
    void createParty_EMPTY_TITLE(String emptyTitle) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of(emptyTitle, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "직관팟 제목이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 제목은 225자를 넘길 수 없다.")
    @Test
    void createParty_EXCEED_TITLE() throws Exception {
        String exceedTitle = "a".repeat(226);
        PartyCreateRequest request = PartyCreateRequest.of(exceedTitle, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "제목의 길이를 1~225자 이내로 작성해 주세요!");
    }

    //  직관팟 생성 partyJoinMethod 이슈
    @DisplayName("직관팟 생성 중 신청 방식은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "partyJoinMethod = {0}")
    void createParty_EMPTY_METHOD(String emptyMethod) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", emptyMethod, "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "신청 방식이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 신청 방식은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"FIRST_COME", "RESERVATION", "ABCDEFG", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "partyJoinMethod = {0}")
    void createParty_INVALID_METHOD(String invalidMethod) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", invalidMethod, "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "잘못된 신청 방식입니다!");
    }

    //  직관팟 생성 partyGender 이슈
    @DisplayName("직관팟 생성 중 참여 원하는 성별은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "partyGender = {0}")
    void createParty_EMPTY_GENDER(String emptyGender) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", emptyGender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "참여 원하는 성별이 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 성별은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"MALE", "FEMALE", "NO_MATTER", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "partyGender = {0}")
    void createParty_INVALID_GENDER(String invalidGender) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", invalidGender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "잘못된 성별 형식입니다!");
    }

    @DisplayName("직관팟 생성 중 성별은 남자만, 여자만, 상관없음 중 하나만 입력해아 한다.")
    @CsvSource(value = {"남자만", "여자만", "상관없음"})
    @ParameterizedTest(name = "partyGender = {0}")
    void createParty_VALID_GENDER(String gender) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", gender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(post("/party")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partyId").value("1"));
    }

    //  직관팟 생성 ageGroup 이슈
    @DisplayName("직관팟 생성 중 참여자 나이는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "age = {0}")
    void createParty_EMPTY_AGE(List<String> emptyAgeList) throws Exception {

        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", emptyAgeList, 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "참여자 나이가 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 참여자 나이는 정해진 양식대로 입력해야 한다.")
    @MethodSource("invalidAgeGroupProvider")
    @ParameterizedTest(name = "age = {0}")
    void createParty_INVALID_AGE(List<String> emptyAgeList) throws Exception {

        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", emptyAgeList, 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "잘못된 참여자 나이입니다!");
    }

    static Stream<Arguments> invalidAgeGroupProvider() {
        return Stream.of(
                Arguments.of(Arrays.asList("AGE_10", "AGE_20")),
                Arguments.of(Arrays.asList("10대", "AGE_10")),
                Arguments.of(Arrays.asList("10대", "20대", "AGE_30")),
                Arguments.of(Arrays.asList("ABCD", "DEFG", "HIJK")),
                Arguments.of(Arrays.asList("1234", "567", "89ABCD")),
                Arguments.of(Arrays.asList("!@#$", "%^&*", "*(*("))
        );
    }

    @DisplayName("직관팟 생성 중 참여자 나이는 정해진 수를 넘을 수 없다.")
    @Test
    void createParty_TOO_MANY_AGE() throws Exception {

        // given
        List<String> tooManyAgeList = List.of("10대", "20대", "30대", "40대", "50대", "60대 이상", "70대", "80대", "90대");
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", tooManyAgeList, 1L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "참여자 나이는 최대 6개까지 가능합니다!");
    }

    //  직관팟 생성 minimumParticipants 이슈
    @DisplayName("직관팟 최소 인원은 필수이다.")
    @Test
    void createParty_EMPTY_MINIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), null, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "최소 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 1 이상이여야 한다.")
    @Test
    void createParty_INVALID_MINIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 0L, 10L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "최소 참여 인원은 1명 이상이여야 합니다!");
    }

    //  직관팟 생성 maximumParticipants 이슈
    @DisplayName("직관팟 최대 인원은 필수이다.")
    @Test
    void createParty_EMPTY_MAXIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, null, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "최대 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 최대 인원보다 클 수 없다.")
    @Test
    void createParty_MINIMUM_MAXIMUM() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 10L, 9L, thumbnailUrl, 1L, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!");
    }

    //  직관팟 생성 thumbnailImageNameList 이슈
    @DisplayName("직관팟 생성 중 사진 파일명은 비어 있어도 된다..")
    @MethodSource("validUrlGroupProvider")
    @ParameterizedTest(name = "url = {0}")
    void createParty_VALID_IMAGE_URL(List<String> validUrl) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, validUrl, 1L, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(post("/party")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partyId").value("1"));
    }

    static Stream<Arguments> validUrlGroupProvider() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of("image.jpg")),
                Arguments.of(List.of("image.png")),
                Arguments.of(Arrays.asList("image.jpg", "image.png")),
                Arguments.of(Arrays.asList("image.jpg", "image.png", "image.webp"))
        );
    }

    @DisplayName("직관팟 생성 중 사진 파일명은 최대 10개까지만 담을 수 있다.")
    @Test
    void createParty_NULL_IMAGE_URL() throws Exception {
        // given
        List<String> tooManyUrlList = List.of("http://image.com", "https://image.com", "ftp://image.com", "http://image.com",
                "https://image.com", "ftp://image.com", "ftp://image.com", "http://image.com",
                "https://image.com", "ftp://image.com", "http://image.com");
        PartyCreateRequest request = PartyCreateRequest.of("title", "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, tooManyUrlList, 1L, "message");
        PartyCreateResponse response = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "썸네일은 최대 10개까지만 가능합니다!");
    }

    //  직관팟 생성 matchId 이슈
    @DisplayName("직관팟 생성 시 경기 ID는 필수이다.")
    @Test
    void createParty_EMPTY_MATCH_ID() throws Exception {
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, thumbnailUrl, null, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "경기 ID는 필수입니다!");
    }

    @DisplayName("직관팟 생성 시 경기 ID는 1 이상이여야 한다.")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidMatchId = {0}")
    void createParty_INVALID_MATCH_ID(String invalidMatchIdStr) throws Exception {
        Long invalidMatchId = Long.valueOf(invalidMatchIdStr);
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"),
                1L, 10L, thumbnailUrl, invalidMatchId, "message");
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "경기 ID는 1 이상이어야 합니다!");
    }

    //  직관팟 생성 message 이슈
    @DisplayName("직관팟 생성 중 소개 문구는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "message = {0}")
    void createParty_EMPTY_MESSAGE(String emptyMessage) throws Exception {
        // given
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, emptyMessage);
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "직관팟 소개 문구가 비어 있습니다!");
    }

    @DisplayName("직관팟 생성 중 소개 문구는 100자 이내여야 한다.")
    @Test
    void createParty_EXCEED_MESSAGE() throws Exception {
        // given
        String exceedMessage = "a".repeat(101);
        PartyCreateRequest request = PartyCreateRequest.of("제목", "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, 1L, exceedMessage);
        PartyCreateResponse mockResponse = PartyCreateResponse.of(1L);
        given(partyService.createParty(any(Long.class), any(PartyCreateRequest.class))).willReturn(mockResponse);

        // when // then
        assertBadRequestOfPartyCreateRequest(request, "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!");
    }

//    @DisplayName("이미지 저장을 위한 Presigned URL을 발급해줄 수 있다.")
//    @Test
//    void generatePresignedUrlForSaveImage() throws Exception {
//        // given
//        PresignedUrlForSaveImageRequest request = new PresignedUrlForSaveImageRequest("image.jpg");
//        given(partyService.generatePresignedUrlForSaveImage(any(PresignedUrlForSaveImageRequest.class)))
//                .willReturn(new PresignedUrlForSaveImageResponse("https://presigned-url.com"));
//
//        // when // then
//        mockMvc.perform(post("/party/presigned-url")
//                        .content(objectMapper.writeValueAsString(request))
//                        .contentType(APPLICATION_JSON)
//                        .with(authentication(token)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.presignedUrl").value("https://presigned-url.com"));
//    }

//    @DisplayName("URL 발급을 위해서 이미지 파일명은 필수이다.")
//    @NullAndEmptySource
//    @ParameterizedTest(name = "url = {0}")
//    void generatePresignedUrlForSaveImage_BLANK_IMAGE_NAME(String blankImageFileName) throws Exception {
//        // given
//        PresignedUrlForSaveImageRequest request = new PresignedUrlForSaveImageRequest(blankImageFileName);
//
//        // when // then
//        mockMvc.perform(post("/party/presigned-url")
//                        .content(objectMapper.writeValueAsString(request))
//                        .contentType(APPLICATION_JSON)
//                        .with(authentication(token)))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
//                .andExpect(jsonPath("$.message").value("이미지 파일명은 필수입니다!"));
//    }

    // 직관팟 반환
    @DisplayName("특정 경기에 대한 직관팟을 가져올 수 있다.")
    @Test
    void getPartiesByMatchId() throws Exception {
        // given
        Long matchId = 1L;
        Long writerId = 1L;
        List<PartyAgeGroup> partyAges = List.of(PartyAgeGroup.AGE_10, PartyAgeGroup.AGE_20);
        List<String> partyThumbnailUrls = List.of("http://party-thumbnail", "http://party-thumbnail2.com");
        List<String> userThumbnailUrls = List.of("http://user-thumbnailUrl", "http://user2-thumbnailUrl");

        PartyInfoResponse response = PartyInfoResponse.of(1L, writerId, "title", PartyJoinMethod.RESERVATION, partyAges, PartyGender.MALE,
                "ZSJ", "남성", 15,
                10L, 14L, partyThumbnailUrls, userThumbnailUrls);

        List<PartyInfoResponse> result = List.of(response);

        given(partyReadOnlyService.getPartyInfoListByMatchId(any(Long.class)))
                .willReturn(result);

        // when // then
        mockMvc.perform(get("/party")
                        .param("matchId", String.valueOf(matchId))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].partyId").value(1L))
                .andExpect(jsonPath("$[0].writerId").value(writerId))
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[0].partyJoinMethod").value("승인제"))
                .andExpect(jsonPath("$[0].partyAges[0]").value("10대"))
                .andExpect(jsonPath("$[0].partyAges[1]").value("20대"))
                .andExpect(jsonPath("$[0].availableGender").value("남자만"))
                .andExpect(jsonPath("$[0].authorName").value("ZSJ"))
                .andExpect(jsonPath("$[0].authorGender").value("남성"))
                .andExpect(jsonPath("$[0].authorAge").value("10대"))
                .andExpect(jsonPath("$[0].currentParticipantsCount").value(10))
                .andExpect(jsonPath("$[0].maximumParticipantsCount").value(14))
                .andExpect(jsonPath("$[0].partyThumbnailUrls[0]").value("http://party-thumbnail"))
                .andExpect(jsonPath("$[0].partyThumbnailUrls[1]").value("http://party-thumbnail2.com"))
                .andExpect(jsonPath("$[0].userThumbnailUrls[0]").value("http://user-thumbnailUrl"))
                .andExpect(jsonPath("$[0].userThumbnailUrls[1]").value("http://user2-thumbnailUrl"));
    }

    @DisplayName("직관팟을 가져올 때 그 경기의 ID는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest
    void getPartiesByMatchId_EMPTY_MATCHID(String emptyMatchIdStr) throws Exception {
        // given
        List<PartyAgeGroup> partyAges = List.of(PartyAgeGroup.AGE_10, PartyAgeGroup.AGE_20);
        List<String> partyThumbnailUrls = List.of("http://party-thumbnail", "http://party-thumbnail2.com");
        List<String> userThumbnailUrls = List.of("http://user-thumbnailUrl", "http://user2-thumbnailUrl");

        PartyInfoResponse response = PartyInfoResponse.of(1L, 1L, "title", PartyJoinMethod.RESERVATION, partyAges, PartyGender.MALE,
                "ZSJ", "남성", 17,
                10L, 14L, partyThumbnailUrls, userThumbnailUrls);

        List<PartyInfoResponse> result = List.of(response);

        given(partyReadOnlyService.getPartyInfoListByMatchId(any(Long.class)))
                .willReturn(result);


        // when // then
        mockMvc.perform(get("/party")
                        .param("matchId", emptyMatchIdStr)
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("경기 ID는 필수입니다!"));
    }

    @DisplayName("직관팟을 가져올 때, 그 경기의 ID는 1 이상이여야 한다.")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest
    void getPartiesByMatchId_INVALID_MATCHID(String invalidMatchIdStr) throws Exception {
        // given
        List<PartyAgeGroup> partyAges = List.of(PartyAgeGroup.AGE_10, PartyAgeGroup.AGE_20);
        List<String> partyThumbnailUrls = List.of("http://party-thumbnail", "http://party-thumbnail2.com");
        List<String> userThumbnailUrls = List.of("http://user-thumbnailUrl", "http://user2-thumbnailUrl");

        PartyInfoResponse response = PartyInfoResponse.of(1L, 1L, "title", PartyJoinMethod.RESERVATION, partyAges, PartyGender.MALE,
                "ZSJ", "남성", 17,
                10L, 14L, partyThumbnailUrls, userThumbnailUrls);

        List<PartyInfoResponse> result = List.of(response);

        given(partyReadOnlyService.getPartyInfoListByMatchId(any(Long.class)))
                .willReturn(result);

        // when // then
        mockMvc.perform(get("/party")
                        .param("matchId", invalidMatchIdStr)
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("ID는 1 이상이여아 합니다!"));
    }

    @DisplayName("특정 경기에 대한 직관팟이 없을 수 있다.")
    @Test
    void getPartiesByMatchId_EMPTY_PARTY() throws Exception {
        // given
        Long matchId = 1L;

        given(partyReadOnlyService.getPartyInfoListByMatchId(any(Long.class)))
                .willReturn(List.of());

        // when // then
        mockMvc.perform(get("/party")
                        .param("matchId", String.valueOf(matchId))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // msa 때문에 관련해서 주석 처리
//    @DisplayName("특정 직관팟에 대한 상세한 정보를 가져올 수 있다.")
//    @Test
//    void getPartyDetail() throws Exception {
//        // given
//        long partyId = 1L;
//        Long writerId = 1L;
//        List<PartyAgeGroup> partyAgeGroup = List.of(PartyAgeGroup.AGE_10, PartyAgeGroup.AGE_20);
//        LocalDateTime matchDate = LocalDateTime.of(2025, 3, 22, 14, 0, 0);
//        List<String> partyThumbnailUrls = List.of("http://party-thumbnail", "http://party-thumbnail2.com");
//        List<String> userThumbnailUrls = List.of("http://user-thumbnailUrl", "http://user2-thumbnailUrl");
//
//        PartyDetailResponse response = PartyDetailResponse.of(1L, writerId, "title", PartyJoinMethod.RESERVATION,
//                "explanation", partyAgeGroup, PartyGender.MALE, "ZSJ", "남성", 25, matchDate,
//                10L, 14L, partyThumbnailUrls, userThumbnailUrls);
//
//        given(partyReadOnlyService.getPartyDetail(partyId))
//                .willReturn(response);
//
//        // when // then
//        mockMvc.perform(get("/party/" + partyId)
//                        .contentType(APPLICATION_JSON)
//                        .with(authentication(token)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("title"))
//                .andExpect(jsonPath("$.partyId").value(partyId))
//                .andExpect(jsonPath("$.writerId").value(writerId))
//                .andExpect(jsonPath("$.partyJoinMethod").value("승인제"))
//                .andExpect(jsonPath("$.partyAgeGroup[0]").value("10대"))
//                .andExpect(jsonPath("$.partyAgeGroup[1]").value("20대"))
//                .andExpect(jsonPath("$.text").value("explanation"))
//                .andExpect(jsonPath("$.availableGender").value("남자만"))
//                .andExpect(jsonPath("$.authorName").value("ZSJ"))
//                .andExpect(jsonPath("$.authorGender").value("남성"))
//                .andExpect(jsonPath("$.authorAge").value("20대"))
//                .andExpect(jsonPath("$.matchDate").value("3.22(토) 오후 2:00"))
//                .andExpect(jsonPath("$.currentParticipantsCount").value(10))
//                .andExpect(jsonPath("$.maximumParticipantsCount").value(14))
//                .andExpect(jsonPath("$.partyThumbnailUrls[0]").value("http://party-thumbnail"))
//                .andExpect(jsonPath("$.partyThumbnailUrls[1]").value("http://party-thumbnail2.com"))
//                .andExpect(jsonPath("$.userThumbnailUrls[0]").value("http://user-thumbnailUrl"))
//                .andExpect(jsonPath("$.userThumbnailUrls[1]").value("http://user2-thumbnailUrl"));
//    }

    @DisplayName("특정 직관팟에 대해 조회할 때, ID는 1 이상이여야 한다.")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidStrId = {0}")
    void getPartyDetail_EMPTY_PARTYID(String invalidIdStr) throws Exception {
        // given

        // when // then
        mockMvc.perform(get("/party/" + invalidIdStr)
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
    }

    @DisplayName("직관팟을 수정할 수 있다.")
    @Test
    void updateParty() throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("title", writerId, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(partyId);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(put("/party/" + partyId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyId").value("1"));
    }

    @DisplayName("직관팟을 수정할 때, 직관팟의 ID는 1 이상이여야 한다..")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
    void updateParty_EMPTY_PARTYID(String invalidPartyIdStr) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("title", writerId, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(partyId);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + invalidPartyIdStr, "직관팟 ID는 1 이상이어야 합니다!");
    }

    @DisplayName("직관팟 수정 중 제목은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "title = {0}")
    void updateParty_EMPTY_TITLE(String emptyTitle) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of(emptyTitle, writerId, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(partyId);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);


        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "직관팟 제목이 비어 있습니다!");
    }

    @DisplayName("직관팟 수정 중 제목은 225자를 넘길 수 없다.")
    @Test
    void updateParty_EXCEED_TITLE() throws Exception {
        String exceedTitle = "a".repeat(226);
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of(exceedTitle, writerId, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(partyId);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "제목의 길이를 1~225자 이내로 작성해 주세요!");
    }

    @DisplayName("직관팟을 수정할 때, 직관팟 작성자의 ID는 1 이상이여야 한다..")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidWriterIdStr = {0}")
    void updateParty_INVALID_WRITERID(String invalidWriterIdStr) throws Exception {
        // given
        long partyId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("title", Long.parseLong(invalidWriterIdStr), "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(partyId);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "작성자 ID는 1 이상이어야 합니다!");
    }

    //  직관팟 수정 partyJoinMethod 이슈
    @DisplayName("직관팟 수정 중 신청 방식은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "partyJoinMethod = {0}")
    void updateParty_EMPTY_METHOD(String emptyMethod) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, emptyMethod, "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "신청 방식이 비어 있습니다!");
    }

    @DisplayName("직관팟 수정 중 신청 방식은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"FIRST_COME", "RESERVATION", "ABCDEFG", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "partyJoinMethod = {0}")
    void updateParty_INVALID_METHOD(String invalidMethod) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, invalidMethod, "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "잘못된 신청 방식입니다!");
    }

    //  직관팟 수정 partyGender 이슈
    @DisplayName("직관팟 수정 중 참여 원하는 성별은 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "partyGender = {0}")
    void updateParty_EMPTY_GENDER(String emptyGender) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", emptyGender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "참여 원하는 성별이 비어 있습니다!");
    }

    @DisplayName("직관팟 수정 중 성별은 정해진 양식대로 입력해야 한다.")
    @CsvSource(value = {"MALE", "FEMALE", "NO_MATTER", "ㄱㄴㄷㄹㅁㅂㅅㅇ", "!@#$%^&", "123456789"})
    @ParameterizedTest(name = "partyGender = {0}")
    void updateParty_INVALID_GENDER(String invalidGender) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", invalidGender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "잘못된 성별 형식입니다!");
    }

    @DisplayName("직관팟 수정 중 성별은 남자만, 여자만, 상관없음 중 하나만 입력해아 한다.")
    @CsvSource(value = {"남자만", "여자만", "상관없음"})
    @ParameterizedTest(name = "partyGender = {0}")
    void updateParty_VALID_GENDER(String gender) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("title", writerId, "선착순", gender, List.of("10대", "20대"), 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(put("/party/" + partyId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyId").value("1"));
    }

    //  직관팟 수정 ageGroup 이슈
    @DisplayName("직관팟 수정 중 참여자 나이는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "age = {0}")
    void updateParty_EMPTY_AGE(List<String> emptyAgeList) throws Exception {

        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", emptyAgeList, 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "참여자 나이가 비어 있습니다!");
    }

    @DisplayName("직관팟 수정 중 참여자 나이는 정해진 양식대로 입력해야 한다.")
    @MethodSource("invalidAgeGroupProvider")
    @ParameterizedTest(name = "age = {0}")
    void updateParty_INVALID_AGE(List<String> emptyAgeList) throws Exception {

        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", emptyAgeList, 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "잘못된 참여자 나이입니다!");
    }

    @DisplayName("직관팟 수정 중 참여자 나이는 정해진 수를 넘을 수 없다.")
    @Test
    void updateParty_TOO_MANY_AGE() throws Exception {

        // given
        long partyId = 1L;
        Long writerId = 1L;
        List<String> tooManyAgeList = List.of("10대", "20대", "30대", "40대", "50대", "60대 이상", "70대", "80대", "90대");
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", tooManyAgeList, 1L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "참여자 나이는 최대 6개까지 가능합니다!");
    }

    //  직관팟 수정 minimumParticipants 이슈
    @DisplayName("직관팟 최소 인원은 필수이다.")
    @Test
    void updateParty_EMPTY_MINIMUM() throws Exception {
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", List.of("10대", "20대"), null, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "최소 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 1 이상이여야 한다.")
    @Test
    void updateParty_INVALID_MINIMUM() throws Exception {
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", List.of("10대", "20대"), 0L, 10L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "최소 참여 인원은 1명 이상이여야 합니다!");
    }

    //  직관팟 수정 maximumParticipants 이슈
    @DisplayName("직관팟 최대 인원은 필수이다.")
    @Test
    void updateParty_EMPTY_MAXIMUM() throws Exception {
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", List.of("10대", "20대"), 1L, null, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "최대 참여 인원이 비어 있습니다!");
    }

    @DisplayName("직관팟 최소 인원은 최대 인원보다 클 수 없다.")
    @Test
    void updateParty_MINIMUM_MAXIMUM() throws Exception {
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", List.of("10대", "20대"), 10L, 9L, thumbnailUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!");
    }

    //  직관팟 수정 thumbnailImageNameList 이슈
    @DisplayName("직관팟 수정 중 사진 파일명은 비어 있어도 된다. ")
    @MethodSource("validUrlGroupProvider")
    @ParameterizedTest(name = "url = {0}")
    void updateParty_VALID_IMAGE_URL(List<String> validUrl) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("title", writerId, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, validUrl, "message");
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(put("/party/" + partyId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyId").value("1"));
    }

//    @DisplayName("직관팟 수정 중 사진 url은 최대 10개까지만 담을 수 있다.")
//    @Test
//    void updateParty_NULL_IMAGE_URL() throws Exception {
//        // given
//        long partyId = 1L;
//        Long writerId = 1L;
//        List<String> tooManyUrlList = List.of("http://image.com", "https://image.com", "ftp://image.com", "http://image.com",
//                "https://image.com", "ftp://image.com", "ftp://image.com", "http://image.com",
//                "https://image.com", "ftp://image.com", "http://image.com");
//        PartyUpdateRequest request = PartyUpdateRequest.of("title", writerId, "선착순", "남자만", List.of("10대", "20대"),
//                1L, 10L, tooManyUrlList, "message");
//        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
//        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);
//
//        // when // then
//        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "썸네일은 최대 10개까지만 가능합니다!");
//    }


    //  직관팟 수정 message 이슈
    @DisplayName("직관팟 수정 중 소개 문구는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest(name = "message = {0}")
    void updateParty_EMPTY_MESSAGE(String emptyMessage) throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, emptyMessage);
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "직관팟 소개 문구가 비어 있습니다!");
    }

    @DisplayName("직관팟 수정 중 소개 문구는 100자 이내여야 한다.")
    @Test
    void updateParty_EXCEED_MESSAGE() throws Exception {
        // given
        long partyId = 1L;
        Long writerId = 1L;
        String exceedMessage = "a".repeat(101);
        PartyUpdateRequest request = PartyUpdateRequest.of("제목", writerId, "선착순", "남자만", List.of("10대", "20대"), 1L, 10L, thumbnailUrl, exceedMessage);
        PartyUpdateResponse response = PartyUpdateResponse.of(1L);
        given(partyService.updateParty(any(Long.class), any(PartyIdRequest.class), any(PartyUpdateRequest.class))).willReturn(response);

        // when // then
        assertBadRequestOfPartyUpdateRequest(request, "/party/" + partyId, "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!");
    }

    @DisplayName("직관팟을 삭제할 수 있다.")
    @Test
    void deleteParty() throws Exception {
        // given
        Long deletedPartyId = 1L;
        PartyDeleteResponse expectedResponse = PartyDeleteResponse.of(deletedPartyId);
        given(partyService.deleteParty(any(Long.class), any(Long.class))).willReturn(expectedResponse);

        // when // then
        mockMvc.perform(patch("/party/" + deletedPartyId)
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deletedPartyId").value("1"));
    }

    @DisplayName("직관팟을 삭제할 때, 직관팟의 ID는 1 이상이여야 한다..")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
    void deleteParty_EMPTY_PARTYID(String invalidPartyIdStr) throws Exception {
        // given
        Long deletedPartyId = 1L;
        PartyDeleteResponse expectedResponse = PartyDeleteResponse.of(deletedPartyId);
        given(partyService.deleteParty(any(Long.class), any(Long.class))).willReturn(expectedResponse);

        // when // then
        mockMvc.perform(patch("/party/" + invalidPartyIdStr)
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
    }

    @DisplayName("직관팟을 종료할 수 있다")
    @Test
    void terminateParty() throws Exception {
        // given
        Long terminatedPartyId = 1L;
        PartyEndResponse expectedResponse = PartyEndResponse.of(terminatedPartyId);
        given(partyService.terminateParty(any(), any(Long.class))).willReturn(expectedResponse);

        // when // then
        mockMvc.perform(patch("/party/" + terminatedPartyId + "/end")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endedPartyId").value("1"));
    }

    @DisplayName("직관팟을 종료할 때, 직관팟의 ID는 1 이상이여야 한다..")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
    void terminateParty_INVALID_PARTYID(String invalidPartyIdStr) throws Exception {
        // given

        // when // then
        mockMvc.perform(patch("/party/" + invalidPartyIdStr + "/end")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
    }

    @DisplayName("선착순 직관팟에 지원할 수 있다.")
    @Test
    void applyPartyFCFS() throws Exception {
        // given
        long partyId = 1L;
        willDoNothing().given(partyApplyFacade).applyParty(any(CustomOAuth2User.class), any(Long.class));

        // when // then
        mockMvc.perform(post("/party/" + partyId + "/apply/fcfs")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("직관팟 가입에 성공했습니다!"));
    }

    @DisplayName("선착순 직관팟에 지원할 때, 직관팟의 ID는 1 이상이여야 한다..")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
    void applyPartyFCFS_INVALID_PARTYID(String invalidPartyIdStr) throws Exception {
        // given

        // when // then
        mockMvc.perform(post("/party/" + invalidPartyIdStr + "/apply/fcfs")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
    }

    @DisplayName("승인제 직관팟에 지원할 수 있다.")
    @Test
    void applyParty() throws Exception {
        // given
        long partyId = 1L;
        PartyApproveApplyRequest request = PartyApproveApplyRequest.of("message");
        PartyApplyResponse response = PartyApplyResponse.of("직관팟 승인에 성공했습니다!");
        given(partyService.applyParty(any(CustomOAuth2User.class), any(Long.class), anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(post("/party/" + partyId + "/apply")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("직관팟 승인에 성공했습니다!"));
    }

    @DisplayName("메시지가 없어도 승인제 직관팟에 지원할 수 있다.")
    @Test
    void applyParty_EMPTY_MESSAGE() throws Exception {
        // given
        long partyId = 1L;
        PartyApproveApplyRequest request = PartyApproveApplyRequest.of(null);
        PartyApplyResponse response = PartyApplyResponse.of("직관팟 승인에 성공했습니다!");
        given(partyService.applyParty(any(CustomOAuth2User.class), any(Long.class), any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(post("/party/" + partyId + "/apply")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("직관팟 승인에 성공했습니다!"));
    }

    @DisplayName("승인제 직관팟을 지원할 때, 직관팟의 ID는 1 이상이여야 한다..")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
    void applyParty_INVALID_PARTYID(String invalidPartyIdStr) throws Exception {
        // given
        PartyApproveApplyRequest request = PartyApproveApplyRequest.of(null);
        PartyApplyResponse response = PartyApplyResponse.of("직관팟 가입에 성공했습니다!");
        given(partyService.applyParty(any(CustomOAuth2User.class), any(Long.class), any()))
                .willReturn(response);

        // when // then
        mockMvc.perform(post("/party/" + invalidPartyIdStr + "/apply")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
    }

    @DisplayName("직관팟을 승인할 수 있다.")
    @Test
    void approveParty() throws Exception {
        // given
        long partyId = 1L;
        PartyApproveRequest request = PartyApproveRequest.of(1L, Boolean.TRUE);
        PartyApproveResponse response = PartyApproveResponse.of("직관팟 가입 신청 승인 성공했습니다!");
        given(partyService.approveParty(any(Long.class), any(Long.class), any(PartyApproveRequest.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(patch("/party/" + partyId + "/approve")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("직관팟 가입 신청 승인 성공했습니다!"));
    }

//    @DisplayName("직관팟을 승인할 때 직관팟의 ID는 1 이상이여야 한다.")
//    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
//    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
//    void approveParty_INVALID_PARTYID(String invalidPartyStr) throws Exception {
//        // given
//        PartyApproveRequest request = PartyApproveRequest.of(1L, Boolean.TRUE);
//
//        // when // then
//        mockMvc.perform(patch("/party/" + invalidPartyStr + "/approve")
//                        .content(objectMapper.writeValueAsString(request))
//                        .contentType(APPLICATION_JSON)
//                        .with(authentication(token)))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
//                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
//    }

    @DisplayName("직관팟을 승인할 때 지원자의 ID는 필수이다.")
    @Test
    void approveParty_EMPTY_APPLICANT_ID() throws Exception {
        // given
        long partyId = 1L;
        PartyApproveRequest request = PartyApproveRequest.of(null, Boolean.TRUE);
        PartyApproveResponse response = PartyApproveResponse.of("직관팟 가입 신청 승인 성공했습니다!");
        given(partyService.approveParty(any(Long.class), any(Long.class), any(PartyApproveRequest.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(patch("/party/" + partyId + "/approve")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("지원자 ID는 필수입니다!"));
    }

    @DisplayName("직관팟을 승인할 때 지원자의 ID는 1 이상이여야 한다.")
    @Test
    void approveParty_NEGATIVE_APPLICANT_ID() throws Exception {
        // given
        long partyId = 1L;
        PartyApproveRequest request = PartyApproveRequest.of(null, Boolean.TRUE);
        PartyApproveResponse response = PartyApproveResponse.of("직관팟 가입 신청 승인 성공했습니다!");
        given(partyService.approveParty(any(Long.class), any(Long.class), any(PartyApproveRequest.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(patch("/party/" + partyId + "/approve")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("지원자 ID는 필수입니다!"));
    }

    @DisplayName("직관팟을 승인할 때 승인 여부는 필수이다")
    @Test
    void approveParty_EMPTY_IS_APPROVED() throws Exception {
        // given
        long partyId = 1L;
        PartyApproveRequest request = PartyApproveRequest.of(1L, null);
        PartyApproveResponse response = PartyApproveResponse.of("직관팟 가입 신청 승인 성공했습니다!");
        given(partyService.approveParty(any(Long.class), any(Long.class), any(PartyApproveRequest.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(patch("/party/" + partyId + "/approve")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("승인 여부는 필수입니다!"));
    }

    @DisplayName("직관팟 신청 인원 목록을 불러올 수 있다.")
    @Test
    void getAppliedUser() throws Exception {
        // given
        long partyId = 1L;
        given(partyReadOnlyService.getAppliedUsers(any(Long.class), any(Long.class)))
                .willReturn(List.of(PartyAppliedUserResponse.of(1L, "name", "10대", "http://image.jpg", "참여 희망합니다!")));

        // when // then
        mockMvc.perform(get("/party/" + partyId + "/approved-applicants")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].ageGroup").value("10대"))
                .andExpect(jsonPath("$[0].thumbnailUrl").value("http://image.jpg"))
                .andExpect(jsonPath("$[0].requireMessage").value("참여 희망합니다!"));
    }

    @DisplayName("직관팟 참여자를 조회할 수 있다.")
    @Test
    void getParticipants() throws Exception {
        // given
        long partyId = 1L;
        String url = "url";
        given(partyReadOnlyService.getParticipants(any(), any()))
                .willReturn(List.of(
                        PartyParticipantsInfoResponse.of(1L, "name", url),
                        PartyParticipantsInfoResponse.of(2L, "name2", url)
                ));

        // when // then
        mockMvc.perform(get("/party/" + partyId + "/participants")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].name").value("name2"));
    }

    @DisplayName("직관팟 참가자를 불러올 때 직관팟의 ID는 1 이상이여야 한다.")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
    void getParticipants_INVALID_PARTYID(String invalidPartyStr) throws Exception {
        // given

        // when // then
        mockMvc.perform(get("/party/" + invalidPartyStr + "/participants")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
    }

//    @DisplayName("직관팟을 승인할 때 직관팟의 ID는 1 이상이여야 한다.")
//    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
//    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
//    void getAppliedUser_INVALID_PARTYID(String invalidPartyStr) throws Exception {
//        // given
//
//        // when // then
//        mockMvc.perform(get("/party/" + invalidPartyStr + "/approved-applicants")
//                        .contentType(APPLICATION_JSON)
//                        .with(authentication(token)))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
//                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
//    }

    @DisplayName("직관팟을 탈퇴할 수 있다.")
    @Test
    void leaveParty() throws Exception {
        // given
        long partyId = 1L;
        PartyLeaveResponse response = PartyLeaveResponse.of("직관팟 탈퇴에 성공하셨습니다!");
        given(partyService.leaveParty(any(), any())).willReturn(response);

        // when // then
        mockMvc.perform(post("/party/" + partyId + "/leave")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("직관팟 탈퇴에 성공하셨습니다!"));
    }

    @DisplayName("직관팟을 탈퇴할 때 직관팟의 ID는 1 이상이여야 한다.")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
    void leaveParty_INVALID_PARTYID(String invalidPartyStr) throws Exception {
        // given

        // when // then
        mockMvc.perform(post("/party/" + invalidPartyStr + "/leave")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
    }

    @DisplayName("직관팟 신청을 취소할 수 있다.")
    @Test
    void cancelParty() throws Exception {
        // given
        long partyId = 1L;
        PartyCancelResponse response = PartyCancelResponse.of("직관팟 신청 취소 성공하셨습니다!");
        given(partyService.cancelParty(any(), any())).willReturn(response);

        // when // then
        mockMvc.perform(patch("/party/" + partyId + "/cancel")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("직관팟 신청 취소 성공하셨습니다!"));
    }

    @DisplayName("직관팟 신청을 취소할 때 직관팟의 ID는 1 이상이여야 한다.")
    @CsvSource(value = {"0", "-1", "-100", "-1000", "-10000"})
    @ParameterizedTest(name = "invalidPartyIdStr = {0}")
    void cancelParty_INVALID_PARTYID(String invalidPartyStr) throws Exception {
        // given

        // when // then
        mockMvc.perform(patch("/party/" + invalidPartyStr + "/cancel")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("직관팟 ID는 1 이상이어야 합니다!"));
    }

    @DisplayName("자신이 신청한 직관팟 현황을 조회할 수 있다.")
    @Test
    void getAppliedParties() throws Exception {
        // given

        given(partyReadOnlyService.getAppliedParties(any()))
                .willReturn(List.of(
                        AppliedPartyResponse.of(1L, "title", List.of("10대", "20대"), "남자만",
                                "신청중", 1L, "ZSJ", "남성", "10대", "http://image.jpg", 5L),

                        AppliedPartyResponse.of(2L, "title2", List.of("30대", "40대"), "여자만",
                                "채팅방 입장!", 2L, "KIM", "여성", "30대", "http://image2.jpg", 1L),

                        AppliedPartyResponse.of(3L, "title3", List.of("50대", "60대 이상"), "상관없음",
                                "승인 거부됨", 3L, "JUNG", "남성", "40대", "http://image3.jpg", 1L)

                ));

        // when // then
        mockMvc.perform(get("/party/applied-parties")
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].partyId").value(1))
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[0].partyAgeGroup[0]").value("10대"))
                .andExpect(jsonPath("$[0].partyAgeGroup[1]").value("20대"))
                .andExpect(jsonPath("$[0].partyGender").value("남자만"))
                .andExpect(jsonPath("$[0].partyJoinRequestStatus").value("신청중"))
                .andExpect(jsonPath("$[0].writerId").value(1L))
                .andExpect(jsonPath("$[0].authorName").value("ZSJ"))
                .andExpect(jsonPath("$[0].authorGender").value("남성"))
                .andExpect(jsonPath("$[0].authorAge").value("10대"))
                .andExpect(jsonPath("$[0].writerThumbnailUrl").value("http://image.jpg"))
                .andExpect(jsonPath("$[0].currentParticipants").value(5))

                .andExpect(jsonPath("$[1].partyId").value(2))
                .andExpect(jsonPath("$[1].title").value("title2"))
                .andExpect(jsonPath("$[1].partyAgeGroup[0]").value("30대"))
                .andExpect(jsonPath("$[1].partyAgeGroup[1]").value("40대"))
                .andExpect(jsonPath("$[1].partyGender").value("여자만"))
                .andExpect(jsonPath("$[1].partyJoinRequestStatus").value("채팅방 입장!"))
                .andExpect(jsonPath("$[1].writerId").value(2L))
                .andExpect(jsonPath("$[1].authorName").value("KIM"))
                .andExpect(jsonPath("$[1].authorGender").value("여성"))
                .andExpect(jsonPath("$[1].authorAge").value("30대"))
                .andExpect(jsonPath("$[1].writerThumbnailUrl").value("http://image2.jpg"))
                .andExpect(jsonPath("$[1].currentParticipants").value(1))

                .andExpect(jsonPath("$[2].partyId").value(3))
                .andExpect(jsonPath("$[2].title").value("title3"))
                .andExpect(jsonPath("$[2].partyAgeGroup[0]").value("50대"))
                .andExpect(jsonPath("$[2].partyAgeGroup[1]").value("60대 이상"))
                .andExpect(jsonPath("$[2].partyGender").value("상관없음"))
                .andExpect(jsonPath("$[2].partyJoinRequestStatus").value("승인 거부됨"))
                .andExpect(jsonPath("$[2].writerId").value(3L))
                .andExpect(jsonPath("$[2].authorName").value("JUNG"))
                .andExpect(jsonPath("$[2].authorGender").value("남성"))
                .andExpect(jsonPath("$[2].authorAge").value("40대"))
                .andExpect(jsonPath("$[2].writerThumbnailUrl").value("http://image3.jpg"))
                .andExpect(jsonPath("$[2].currentParticipants").value(1));
    }

    private void assertBadRequestOfPartyCreateRequest(PartyCreateRequest request, String expectedResult) throws Exception {
        mockMvc.perform(post("/party")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(expectedResult));
    }

    private void assertBadRequestOfPartyUpdateRequest(PartyUpdateRequest request, String requestUri, String expectedResult) throws Exception {
        mockMvc.perform(put(requestUri)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(expectedResult));
    }
}
