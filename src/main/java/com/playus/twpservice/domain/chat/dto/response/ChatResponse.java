//package com.playus.twpservice.domain.chat.dto.response;
//
//import com.playus.twpservice.domain.chat.entity.ChatParticipant;
//import lombok.Builder;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Builder
//public record ChatResponse(
//        Long userId,
//        LocalDateTime disconnectedAt,
//        List<ChattingMessageResponse> chattingMessage,
//        ChatPageableResponse pageable
//) {
//    public static ChatResponse of(ChatParticipant chatParticipant, List<ChattingMessageResponse> chattingMessages, ChatPageableResponse chatPageableResponse) {
//        return ChatResponse.builder()
//                .userId(chatParticipant.getUserId())
//                .disconnectedAt(chatParticipant.getDisconnectedAt())
//                .chattingMessage(chattingMessages)
//                .pageable(chatPageableResponse)
//                .build();
//    }
//}
