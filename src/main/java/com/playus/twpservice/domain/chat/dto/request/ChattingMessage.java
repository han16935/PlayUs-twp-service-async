//package com.playus.twpservice.domain.chat.dto.request;
//
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.playus.twpservice.domain.chat.dto.response.ReadMessageRange;
//import com.playus.twpservice.domain.chat.document.ChatMessage;
//import com.playus.twpservice.domain.chat.entity.enums.MessageType;
//import lombok.Builder;
//
//import java.time.LocalDateTime;
//
//@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public record ChattingMessage(
//        String messageId,
//        Long chatRoomId,
//        Long senderId,
//        String senderName,
//        String message,
//        String profileImageUrl,
//        ReadMessageRange range,
//        Long unreadCount,
//        LocalDateTime lastReadAt,
//        MessageType messageType
//) {
//    public static ChattingMessage from(ChatMessage chatMessage) {
//        return ChattingMessage.builder()
//                .messageId(chatMessage.getId())
//                .chatRoomId(chatMessage.getChatRoomId())
//                .senderId(chatMessage.getSenderId())
//                .senderName(chatMessage.getSenderName())
//                .message(chatMessage.getMessage())
//                .profileImageUrl(chatMessage.getProfileImageUrl())
//                .unreadCount(chatMessage.getUnreadCount())
//                .lastReadAt(chatMessage.getLastReadAt())
//                .messageType(chatMessage.getMessageType())
//                .build();
//    }
//
//    public static ChattingMessage of(long roomId, Long senderId, String senderName, ReadMessageRange range, MessageType messageType) {
//        return ChattingMessage.builder()
//                .chatRoomId(roomId)
//                .senderId(senderId)
//                .senderName(senderName)
//                .range(range)
//                .lastReadAt(LocalDateTime.now())
//                .messageType(messageType)
//                .build();
//    }
//}
