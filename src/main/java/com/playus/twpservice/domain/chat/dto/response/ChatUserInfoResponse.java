//package com.playus.twpservice.domain.chat.dto.response;
//
//import lombok.Builder;
//
//import java.util.List;
//
//@Builder
//public record ChatUserInfoResponse(
//        Long roomId,
//        Long totalParticipantCount,
//        ChatUserResponse roomMaster,
//        List<ChatUserResponse> participants
//) {
//    public static ChatUserInfoResponse of(Long roomId, Long totalParticipantCount, ChatUserResponse roomMaster, List<ChatUserResponse> participants) {
//        return ChatUserInfoResponse.builder()
//                .roomId(roomId)
//                .totalParticipantCount(totalParticipantCount)
//                .roomMaster(roomMaster)
//                .participants(participants)
//                .build();
//    }
//}
