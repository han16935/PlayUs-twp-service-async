package com.playus.twpservice.domain.party.specification;

import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.party.dto.appliedparty.AppliedPartyResponse;
import com.playus.twpservice.domain.party.dto.applieduser.PartyAppliedUserResponse;
import com.playus.twpservice.domain.party.dto.apply.PartyApplyResponse;
import com.playus.twpservice.domain.party.dto.apply.PartyApproveApplyRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveResponse;
import com.playus.twpservice.domain.party.dto.cancel.PartyCancelResponse;
import com.playus.twpservice.domain.party.dto.create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.create.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.delete.PartyDeleteResponse;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailRequest;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailResponse;
import com.playus.twpservice.domain.party.dto.end.PartyEndResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoRequest;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.common.request.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.leave.PartyLeaveResponse;
import com.playus.twpservice.domain.party.dto.participants.PartyParticipantsInfoResponse;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface PartyControllerSpecification {


    @Tag(name = "Party Post", description = "직관팟 생성 API")
    @Operation(
            summary = "직관팟 생성",
            description = "로그인한 사용자가 작성자로서 직관팟을 생성합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = @Parameter(
                    name = "Access",
                    description = "JWT Access Token (쿠키)",
                    in = ParameterIn.COOKIE,
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            ),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 생성 요청 예시",
                                    value = """
                                            {
                                              "title": "롯데 vs LG 직관 같이 가요!",
                                              "partyJoinMethod": "선착순",
                                              "partyGender": "남자만",
                                              "ageGroup": ["10대", "20대"],
                                              "minimumParticipants": 3,
                                              "maximumParticipants": 5,
                                              "thumbnailImageNameList": ["party.jpg"],
                                              "matchId" : "1",
                                              "message": "재밌게 응원할 분 구해요!"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "생성 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 생성 응답 예시",
                                    value = """
                                            {
                                              "partyId": 1,
                                              "chatRoomId": "chatRoomId"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 최대 참여 인원보다 클 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 제목이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 제목이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 제목이 1~225자 범위 이외일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "제목의 길이를 1~225자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 신청 방식이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "신청 방식이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 신청 방식이 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 신청 방식입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 성별이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여 원하는 성별이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 성별이 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 성별 형식입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 참여자 나이가 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여자 나이가 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 참여자 나이가 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 참여자 나이입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 생성 시 참여자 나이 개수가 기존에 정의되어 있던 나이를 초과했을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여자 나이는 최대 6개까지 가능합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 1명 아래일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원은 1명 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최대 참여 인원이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최대 참여 인원이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최대 참여 인원이 1명 아래일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최대 참여 인원은 1명 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "썸네일 이미지가 10개를 넘어갈 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "썸네일은 최대 10개까지만 가능합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "썸네일 이미지명이 빈 문자열일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "사진 파일명이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 비어있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "경기 ID는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 1 미만일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "경기 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 소개 문구가 비어있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 소개 문구가 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 소개 문구가 1~100자 사이가 아닐 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "이미 특정 경기에 대해 직관팟을 만들었을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "하나의 경기에 대해 하나의 직관팟만 만들 수 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<PartyCreateResponse> createParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                                    @Valid @Parameter(description = "직관팟 생성 요청", required = true) PartyCreateRequest request);


    @Tag(name = "Party Get", description = "직관팟 조회 API")
    @Operation(
            summary = "직관팟 조회 API",
            description = "특정 경기에 대한 모든 직관팟을 조회합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "matchId",
                            description = "경기 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 조회 응답 예시",
                                    value = """
                                                                    [
                                                                       {
                                                                         "partyId": 1,
                                                                         "title": "title",
                                                                         "partyJoinMethod": "승인제",
                                                                         "partyAges": ["10대", "20대"],
                                                                         "availableGender": "남자만",
                                                                         "authorName": "ZSJ",
                                                                         "authorGender": "남성",
                                                                         "currentParticipantsCount": 10,
                                                                         "maximumParticipantsCount": 14,
                                                                         "partyThumbnailUrls": [
                                                                           "http://party-thumbnail",
                                                                           "http://party-thumbnail2"
                                                                         ],
                                                                         "userThumbnailUrls": [
                                                                           "http://user-thumbnailUrl",
                                                                           "http://user2-thumbnailUrl"
                                                                         ]
                                                                       },
                                            
                                                                          {
                                                                             "partyId": 2,
                                                                             "title": "title2",
                                                                             "partyJoinMethod": "선착순",
                                                                             "partyAges": ["30대"],
                                                                             "availableGender": "여자만",
                                                                             "authorName": "ZSJ",
                                                                             "authorGender": "남성",
                                                                             "currentParticipantsCount": 1,
                                                                             "maximumParticipantsCount": 5,
                                                                             "partyThumbnailUrls": [
                                            
                                                                             ],
                                                                             "userThumbnailUrls": [
                                            
                                                                             ]
                                                                          }
                                                                     ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 없을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "경기 ID는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    List<PartyInfoResponse> getPartiesByMatchId(@Valid @Parameter(description = "직관팟 리스트 요청", hidden = true) PartyInfoRequest request);

    @Tag(name = "Party Get", description = "직관팟 상세정보 조회 API")
    @Operation(
            summary = "직관팟 상세정보 조회 API",
            description = "특정 직관팟에 대한 자세한 정보를 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 상세정보 조회 응답 예시",
                                    value = """
                                            {
                                              "partyId": 1,
                                              "title": "title",
                                              "partyJoinMethod": "승인제",
                                              "text": "explanation",
                                              "partyAges": [
                                                "10대",
                                                "20대"
                                              ],
                                              "availableGender": "남자만",
                                              "authorName": "ZSJ",
                                              "authorGender": "남성",
                                              "currentParticipantsCount": 10,
                                              "maximumParticipantsCount": 14,
                                              "partyThumbnailUrls": [
                                                "http://party-thumbnail",
                                                "http://party-thumbnail2.com"
                                              ],
                                              "userThumbnailUrls": [
                                                "http://user-thumbnailUrl",
                                                "http://user2-thumbnailUrl"
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "이미 종료된 직관팟을 조회하려 할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "이미 종료된 직관팟입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyDetailResponse getPartyDetail(@Valid @Parameter(description = "직관팟 상세정보 요청", hidden = true) PartyDetailRequest request);


    @Tag(name = "Party Put", description = "직관팟 수정 API")
    @Operation(
            summary = "직관팟 수정",
            description = "직관팟 작성자인 로그인한 유저가 직관팟을 수정합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 수정 요청 예시",
                                    value = """
                                            {
                                              "title": "롯데 vs LG 직관 같이 가요!",
                                              "writerId" : 1,
                                              "partyJoinMethod": "선착순",
                                              "partyGender": "남자만",
                                              "ageGroup": ["10대", "20대"],
                                              "minimumParticipants": 3,
                                              "maximumParticipants": 5,
                                              "thumbnailUrl": ["https://image.example.com/party.jpg"],
                                              "message": "재밌게 응원할 분 구해요!"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 수정 응답 예시",
                                    value = """
                                            {
                                              "partyId": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 최대 참여 인원보다 클 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원은 최대 참여 인원보다 클 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 제목이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 제목이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 제목이 1~225자 범위 이외일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "제목의 길이를 1~225자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "작성자 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "작성자 ID는 1 이상이어야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 신청 방식이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "신청 방식이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 신청 방식이 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 신청 방식입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 성별이 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여 원하는 성별이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 성별이 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 성별 형식입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 참여자 나이가 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여자 나이가 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 참여자 나이가 기존에 정의되어 있던 것이 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "잘못된 참여자 나이입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 수정 시 참여자 나이 개수가 기존에 정의되어 있던 나이를 초과했을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "참여자 나이는 최대 6개까지 가능합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최소 참여 인원이 1명 아래일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최소 참여 인원은 1명 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "최대 참여 인원이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최대 참여 인원이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "최대 참여 인원이 1명 아래일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "최대 참여 인원은 1명 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "썸네일 이미지가 10개를 넘어갈 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "썸네일은 최대 10개까지만 가능합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "썸네일 이미지명이 빈 문자열일 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "사진 파일명이 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 소개 문구가 비어있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 소개 문구가 비어 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 소개 문구가 1~100자 사이가 아닐 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 소개 문구의 길이를 1~100자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "로그인 유저가 직관팟 작성자가 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "직관팟 작성자가 아니면 수정할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "잘못된 직관팟 번호입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyUpdateResponse updateParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                    @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", hidden = true) PartyIdRequest idRequest,
                                    @Valid @Parameter(description = "직관팟 수정 요청", required = true) PartyUpdateRequest request);

    @Tag(name = "Party Patch", description = "직관팟 삭제 API")
    @Operation(
            summary = "직관팟 삭제",
            description = "직관팟 작성자인 로그인한 유저가 직관팟을 삭제합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 삭제 응답 예시",
                                    value = """
                                            {
                                              "deletedPartyId": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "로그인 유저가 직관팟 작성자가 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "직관팟 작성자가 아니면 수정할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "잘못된 직관팟 번호입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyDeleteResponse deleteParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                    @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", hidden = true) PartyIdRequest idRequest);


    @Tag(name = "Party Patch", description = "직관팟 종료 API")
    @Operation(
            summary = "직관팟 종료",
            description = "직관팟 작성자인 로그인한 유저가 직관팟을 종료합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "종료 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 종료 응답 예시",
                                    value = """
                                            {
                                              "endedPartyId": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "로그인 유저가 직관팟 작성자가 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "작성자가 아니면 직관팟을 종료할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "이미 종료된 직관팟을 종료 시도할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "이미 종료된 직관팟입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyEndResponse terminateParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                    @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", hidden = true) PartyIdRequest idRequest);

    @Tag(name = "Party Post", description = "직관팟 선착순 신청 API")
    @Operation(
            summary = "직관팟 선착순 신청",
            description = "선착순 승인제인 직관팟에 신청합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "신청 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 신청 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 가입에 성공하셨습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 작성자가 직관팟 지원할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 작성자는 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "거절된 사용자가 다시 지원하려는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "신청이 거절되었으면 다시 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "잘못된 직관팟 번호입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "채팅방 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "채팅방이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "직관팟 정원이 초과될 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "직관팟 정원이 초과되었습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "이미 가입 (혹은 대기) 상태인 직관팟에 다시 신청할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "이미 가입된 직관팟입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyApplyResponse applyPartyFCFS(@Parameter(hidden = true) CustomOAuth2User principal,
                                      @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", hidden = true) PartyIdRequest idRequest);


    @Tag(name = "Party Post", description = "승인제 직관팟 신청 API")
    @Operation(
            summary = "직관팟 승인제 신청",
            description = "승인제 직관팟에 신청합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 신청 요청 예시",
                                    value = """
                                            {
                                              "requireMessage" : "직관팟 참여하고 싶습니다!"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "신청 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 신청 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 신청에 성공하셨습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 작성자가 직관팟 지원할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 작성자는 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "거절된 사용자가 다시 지원하려는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "신청이 거절되었으면 다시 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "잘못된 직관팟 번호입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "채팅방 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "채팅방이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "직관팟 정원이 초과될 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "직관팟 정원이 초과되었습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "이미 가입 (혹은 대기) 상태인 직관팟에 다시 신청할 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "이미 가입된 직관팟입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyApplyResponse applyParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                  @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", hidden = true) PartyIdRequest idRequest,
                                  @Valid @Parameter(description = "직관팟 신청 시 방장에게 보여줄 requireMessage 작성") PartyApproveApplyRequest request);


    @Tag(name = "Party Patch", description = "방장으로서 승인제 직관팟 신청 유저 승인 API")
    @Operation(
            summary = "승인제 직관팟 신청 승인/거절",
            description = "직관팟 신청자에 대해 승인 여부를 결정합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "abcd"
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 승인 여부 결정",
                                    value = """
                                            {
                                              "applicantUserId": 1,
                                              "isApproved" : true
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "승인",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 승인 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 가입 신청 승인 성공했습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "200", description = "거부",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "승인제 직관팟 거부 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 가입 신청 거절 성공했습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "지원자 ID가 비어있는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "지원자 ID는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "지원자 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "지원자 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "지원자 승인 여부가 비어있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "승인 여부는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "선착순 직관팟에 승인 요청 보낼 시 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "선착순 모집인 직관팟에는 승인 요청을 보낼 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 방장이 아닌 사람이 승인 API 날릴 시 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "작성자가 아니면 승인할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "거절된 사용자가 다시 지원하려는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "신청이 거절되었으면 다시 지원할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "403", description = "직관팟 성별 조건에 맞지 않는 사람이 들어올 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "직관팟 성별에 맞지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "직관팟 나이 조건에 맞지 않는 사람이 들어올 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "직관팟 나이에 맞지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "이전에 지원한 기록이 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟 지원자가 아닙니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "채팅방을 찾을 수 없을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "채팅방이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "직관팟 정원이 초과될 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 409,
                                              "status": "CONFLICT",
                                              "message": "직관팟 정원이 초과되었습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyApproveResponse approveParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                      @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", hidden = true) PartyIdRequest idRequest,
                                      @Valid @Parameter(description = "방장이 승인할 userId와 승인 여부 작성") PartyApproveRequest request);


    @Tag(name = "Party Get", description = "직관팟 신청 유저 조회 API")
    @Operation(
            summary = "직관팟 신청 유저 조회 API",
            description = "특정 승인제 직관팟에 대해 신청한 모든 유저를 조회합니다",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 신청 유저 조회 응답 예시",
                                    value = """
                                                                    [
                                                                       {
                                                                         "userId": 1,
                                                                         "name": "ZSJ",
                                                                         "ageGroup": "20대",
                                                                         "thumbnailUrl" : "http://thumbnailUrl",
                                                                         "requireMessage" : "참여 희망합니다!"
                                                                       },
                                            
                                                                       {
                                                                         "userId": 2,
                                                                         "name": "KIM",
                                                                         "ageGroup": "30대",
                                                                         "thumbnailUrl" : "http://thumbnailUrl2",
                                                                         "requireMessage" : "같이 즐겨봐요!"
                                                                       },
                                                                     ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "직관팟 방장이 아닌 자가 직관팟 지원자 조회 시도할 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "방장이 아니면 직관팟 지원자를 조회할 수 없습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    List<PartyAppliedUserResponse> getAppliedUsers(
            @Parameter(hidden = true) CustomOAuth2User principal,
            @Parameter(description = "직관팟 ID", required = true) @PathVariable Long partyId);


    @Tag(name = "Party Get", description = "유저 평가 위한 직관팟 참가자 조회 API")
    @Operation(
            summary = "유저 평가 위한 직관팟 참가자 조회 API",
            description = "직관팟 종료 후 유저 평가 위해 로그인 유저를 제외한 직관팟 참가자를 조회합니다",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 참가자 조회 응답 예시",
                                    value = """
                                                                    [
                                                                       {
                                                                         "userId": 1,
                                                                         "name": "ZSJ"
                                                                       },
                                            
                                                                       {
                                                                         "userId": 2,
                                                                         "name": "KIM"
                                                                       },
                                                                     ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "로그인 유저가 직관팟 참가자가 아닐 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "이 직관팟에 참여하지 않은 인원입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    List<PartyParticipantsInfoResponse> getParticipants(
            @Parameter(hidden = true) CustomOAuth2User principal,
            @Parameter(description = "직관팟 ID", hidden = true) PartyIdRequest idRequest);


    @Tag(name = "Party Post", description = "직관팟 탈퇴 API")
    @Operation(
            summary = "직관팟 탈퇴 API",
            description = "직관팟 인원 (방장 X) 으로서 참여하고 있는 특정 직관팟에 탈퇴할 수 있다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "승인",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 탈퇴 API 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 탈퇴에 성공하셨습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "직관팟 방장이 직관팟 탈퇴 시도할 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "방장은 직관팟을 삭제해 주세요!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "직관팟 참여 대기 상태인 유저가 탈퇴 시도할 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "대기 상태인 유저는 직관팟 신청을 취소해주세요!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "직관팟 참여 거절된 상태인 유저가 탈퇴 시도할 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "직관팟 참여가 이미 거절되었습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟에 참여하지 않은 사람이 직관팟 탈퇴하는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟에 참여한 사람만 탈퇴할 수 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyLeaveResponse leaveParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                  @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", hidden = true) PartyIdRequest idRequest);


    @Tag(name = "Party Patch", description = "직관팟 신청 취소 API")
    @Operation(
            summary = "직관팟 신청 취소 API",
            description = "직관팟 신청 후 아직 방장에게 승인받지 못한 특정 직관팟에 대한 신청을 취소할 수 있다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "partyId",
                            in = ParameterIn.PATH,
                            description = "직관팟 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "승인",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 신청 취소 API 응답 예시",
                                    value = """
                                            {
                                              "message": "직관팟 신청 취소 성공하셨습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "선착순 직관팟에 대해 API를 호출했을 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "선착순 직관팟에는 지원하지 않는 기능입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "직관팟 방장이 직관팟 신청 취소 시도할 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "방장은 직관팟을 삭제해 주세요!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "직관팟에 이미 참여된 유저가 신청 취소 시도할 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "이미 직관팟 회원입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "직관팟 참여 거절된 상태인 유저가 신청 취소 시도할 경우",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 403,
                                              "status": "FORBIDDEN",
                                              "message": "직관팟 참여가 이미 거절되었습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 ID로 직관팟을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "직관팟 참여 신청하지 않은 사람이 직관팟 취소하는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관팟에 신청한 사람만 탈퇴할 수 있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PartyCancelResponse cancelParty(@Parameter(hidden = true) CustomOAuth2User principal,
                                    @Valid @Parameter(description = "API 경로로 들어오는 직관팟 ID 위해 작성", hidden = true) PartyIdRequest idRequest);


    @Tag(name = "Party Get", description = "신청한 직관팟 현황 조회 API")
    @Operation(
            summary = "신청한 직관팟 현황 조회 API",
            description = "본인이 신청한 직관팟 현황을 조회합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관팟 조회 응답 예시",
                                    value = """
                                                                    [
                                                                       {
                                                                             "partyId": 1,
                                                                             "title": "야구 직관팟",
                                                                             "partyAgeGroup": ["10대", "20대"],
                                                                             "partyGender": "남자만",
                                                                             "partyJoinRequestStatus": "신청중",
                                                                             "writerId": 2,
                                                                             "authorName": "ZSJ",
                                                                             "authorGender": "MALE",
                                                                             "authorAge": "20대",
                                                                             "writerThumbnailUrl": "https://example.com/profile.jpg",
                                                                             "currentParticipants": 5
                                                                        },
                                            
                                                                        {
                                                                             "partyId": 3,
                                                                             "title": "축구 직관팟",
                                                                             "partyAgeGroup": ["30대", "40대"],
                                                                             "partyGender": "여자만",
                                                                             "partyJoinRequestStatus": "승인 거부됨",
                                                                             "writerId": 4,
                                                                             "authorName": "ABC",
                                                                             "authorGender": "FEMALE",
                                                                             "authorAge": "30대",
                                                                             "writerThumbnailUrl": "https://example.com/profile.jpg",
                                                                             "currentParticipants": 10
                                                                        }
                                                                     ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "경기 ID가 없을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "경기 ID는 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관팟 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "직관팟 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    List<AppliedPartyResponse> getAppliedParties(@Parameter(hidden = true) CustomOAuth2User principal);


}
