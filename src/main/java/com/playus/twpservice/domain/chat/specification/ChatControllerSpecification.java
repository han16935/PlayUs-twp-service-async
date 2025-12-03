//package com.playus.twpservice.domain.chat.specification;
//
//import com.playus.twpservice.domain.chat.dto.request.ChatMessageRequest;
//import com.playus.twpservice.domain.chat.dto.response.ChatResponse;
//import com.playus.twpservice.domain.chat.dto.response.ChatUserInfoResponse;
//import com.playus.twpservice.domain.common.security.CustomOAuth2User;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.ExampleObject;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Max;
//import jakarta.validation.constraints.Min;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//
//import java.time.LocalDateTime;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//public interface ChatControllerSpecification {
//
//    @Tag(name = "Chat Get", description = "채팅 메시지 조회 API")
//    @Operation(
//            summary = "채팅방 재입장시 이전 메시지 조회",
//            description = "채팅방 재입장시 이전 메시지를 조회하기 위한 API입니다.",
//            security = @SecurityRequirement(name = "Access"),
//            parameters = {
//                    @Parameter(
//                            name = "Access",
//                            description = "JWT Access Token (쿠키)",
//                            in = ParameterIn.COOKIE,
//                            required = true,
//                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
//                    ),
//                    @Parameter(
//                            name = "roomId",
//                            description = "채팅방 ID",
//                            in = ParameterIn.PATH,
//                            required = true,
//                            example = "1"
//                    ),
//                    @Parameter(
//                            name = "pageNumber",
//                            description = "페이지 번호",
//                            required = true,
//                            example = "0"
//                    ),
//                    @Parameter(
//                            name = "pageSize",
//                            description = "페이지 크기",
//                            required = true,
//                            example = "20"
//                    ),
//                    @Parameter(
//                            name = "lastMessageTimeStamp",
//                            description = "마지막 메시지 타임스탬프",
//                            required = false,
//                            example = "2024-03-22T14:00:00"
//                    )
//            }
//    )
//    @ApiResponse(
//            responseCode = "200", description = "조회 성공",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            name = "채팅 메시지 조회 응답 예시",
//                            value = """
//                                    {
//                                      "messages": [
//                                        {
//                                          "messageId": 1,
//                                          "senderId": 1,
//                                          "senderName": "홍길동",
//                                          "content": "안녕하세요!",
//                                          "sendTime": "2024-03-22T14:00:00"
//                                        }
//                                      ],
//                                      "hasNext": true
//                                    }
//                                    """
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "401", description = "인증 실패",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 401,
//                                      "status": "UNAUTHORIZED",
//                                      "message": "유효하지 않은 토큰입니다."
//                                    }
//                                    """
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "404", description = "채팅방을 찾을 수 없는 경우",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 404,
//                                      "status": "NOT_FOUND",
//                                      "message": "채팅방이 존재하지 않습니다!"
//                                    }
//                                    """
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "500", description = "서버 내부 오류",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 500,
//                                      "status": "INTERNAL_SERVER_ERROR",
//                                      "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
//                                    }
//                                    """
//                    )
//            )
//    )
//    ResponseEntity<ChatResponse> getChatMessages(@Parameter(hidden = true) CustomOAuth2User principal,
//                                                 @Parameter(description = "채팅방 ID", required = true) Long roomId,
//                                                 @Parameter(description = "페이지 번호", required = true) @Min(0) int pageNumber,
//                                                 @Parameter(description = "페이지 크기", required = true) @Min(0) @Max(100) int pageSize,
//                                                 @Parameter(description = "마지막 메시지 타임스탬프") LocalDateTime lastMessageTimeStamp);
//
//    @Tag(name = "Chat Get", description = "채팅방 참여자 정보 조회 API")
//    @Operation(
//            summary = "채팅방 참여자 정보 조회",
//            description = "채팅방의 총 참여자 수와 참여 멤버를 조회하기 위한 API입니다.",
//            security = @SecurityRequirement(name = "Access"),
//            parameters = {
//                    @Parameter(
//                            name = "Access",
//                            description = "JWT Access Token (쿠키)",
//                            in = ParameterIn.COOKIE,
//                            required = true,
//                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
//                    ),
//                    @Parameter(
//                            name = "roomId",
//                            description = "채팅방 ID",
//                            in = ParameterIn.PATH,
//                            required = true,
//                            example = "1"
//                    )
//            }
//    )
//    @ApiResponse(
//            responseCode = "200", description = "조회 성공",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            name = "채팅방 참여자 정보 조회 응답 예시",
//                            value = """
//                                    {
//                                      "totalParticipants": 5,
//                                      "participants": [
//                                        {
//                                          "userId": 1,
//                                          "name": "홍길동",
//                                          "thumbnailUrl": "https://example.com/profile.jpg"
//                                        }
//                                      ]
//                                    }
//                                    """
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "401", description = "인증 실패",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 401,
//                                      "status": "UNAUTHORIZED",
//                                      "message": "유효하지 않은 토큰입니다."
//                                    }
//                                    """
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "404", description = "채팅방을 찾을 수 없는 경우",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 404,
//                                      "status": "NOT_FOUND",
//                                      "message": "채팅방이 존재하지 않습니다!"
//                                    }
//                                    """
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "500", description = "서버 내부 오류",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 500,
//                                      "status": "INTERNAL_SERVER_ERROR",
//                                      "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
//                                    }
//                                    """
//                    )
//            )
//    )
//    ResponseEntity<ChatUserInfoResponse> getChatParticipants(@Parameter(hidden = true) CustomOAuth2User principal,
//                                                             @Parameter(description = "채팅방 ID", required = true) Long roomId);
//
//    @Tag(name = "Chat Delete", description = "채팅방 퇴장 API")
//    @Operation(
//            summary = "채팅방 퇴장",
//            description = "채팅방을 퇴장하는 API입니다. 퇴장시 STOMP UNSUBSCRIBE를 꼭 해주세요",
//            security = @SecurityRequirement(name = "Access"),
//            parameters = {
//                    @Parameter(
//                            name = "Access",
//                            description = "JWT Access Token (쿠키)",
//                            in = ParameterIn.COOKIE,
//                            required = true,
//                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
//                    ),
//                    @Parameter(
//                            name = "roomId",
//                            description = "채팅방 ID",
//                            in = ParameterIn.PATH,
//                            required = true,
//                            example = "1"
//                    )
//            }
//    )
//    @ApiResponse(
//            responseCode = "200", description = "퇴장 성공"
//    )
//    @ApiResponse(
//            responseCode = "401", description = "인증 실패",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 401,
//                                      "status": "UNAUTHORIZED",
//                                      "message": "유효하지 않은 토큰입니다."
//                                    }
//                                    """
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "404", description = "채팅방을 찾을 수 없는 경우",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 404,
//                                      "status": "NOT_FOUND",
//                                      "message": "채팅방이 존재하지 않습니다!"
//                                    }
//                                    """
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "500", description = "서버 내부 오류",
//            content = @Content(
//                    mediaType = APPLICATION_JSON_VALUE,
//                    examples = @ExampleObject(
//                            value = """
//                                    {
//                                      "code": 500,
//                                      "status": "INTERNAL_SERVER_ERROR",
//                                      "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
//                                    }
//                                    """
//                    )
//            )
//    )
//    ResponseEntity<Void> exitChatRoom(@Parameter(hidden = true) CustomOAuth2User principal,
//                                      @Parameter(description = "채팅방 ID", required = true) Long roomId);
//
//    void message(@Valid ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor);
//}
