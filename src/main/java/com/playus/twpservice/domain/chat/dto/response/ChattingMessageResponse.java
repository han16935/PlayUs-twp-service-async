//package com.playus.twpservice.domain.chat.dto.response;
//
//import com.playus.twpservice.domain.chat.document.ChatMessage;
//import com.playus.twpservice.domain.chat.entity.enums.MessageType;
//import lombok.Builder;
//
//import java.time.LocalDateTime;
//
//@Builder
//public record ChattingMessageResponse(
//        String messageId,
//        Long senderId,
//        String senderName,
//        String message,
//        String profileImageUrl,
//        Long unreadCount,
//        LocalDateTime lastReadAt,
//        MessageType messageType
//) {
//    public static ChattingMessageResponse from(ChatMessage chatMessage) {
//        return ChattingMessageResponse.builder()
//                .messageId(chatMessage.getId())
//                .senderId(chatMessage.getSenderId())
//                .senderName(chatMessage.getSenderName())
//                .message(chatMessage.getMessage())
//                .profileImageUrl(chatMessage.getProfileImageUrl())
//                .unreadCount(chatMessage.getUnreadCount())
//                .lastReadAt(chatMessage.getLastReadAt())
//                .messageType(chatMessage.getMessageType())
//                .build();
//    }
//}
