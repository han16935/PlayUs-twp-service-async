//package com.playus.twpservice.domain.chat.document;
//
//import com.playus.twpservice.domain.chat.dto.request.ChatMessageRequest;
//import com.playus.twpservice.domain.chat.entity.enums.MessageType;
//import com.playus.twpservice.domain.common.data.BaseMongoTimeEntity;
//import jakarta.validation.constraints.Min;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import lombok.Builder;
//import lombok.Getter;
//
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Document(collection = "chat_messages")
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class ChatMessage extends BaseMongoTimeEntity {
//
//    @Id
//    private String id;
//
//    @NotNull
//    @Field(name = "sender_id")
//    private Long senderId;
//
//    @NotNull
//    @Field(name = "sender_name")
//    private String senderName;
//
//    @Field(name = "profile_image_url")
//    private String profileImageUrl;
//
//    @NotNull
//    @Field(name = "chat_room_id")
//    private Long chatRoomId;
//
//    @NotBlank
//    @Size(min = 1, max = 500)
//    private String message;
//
//    @NotNull
//    @Min(0)
//    @Field(name = "unread_count")
//    private Long unreadCount;
//
//    @NotNull
//    @Field(name = "message_type")
//    private MessageType messageType;
//
//    @NotNull
//    @Field(name = "last_read_at")
//    private LocalDateTime lastReadAt;
//
//    @Builder
//    private ChatMessage(Long senderId, String senderName, String profileImageUrl, Long chatRoomId, String message, Long unreadCount, MessageType messageType, LocalDateTime lastReadAt) {
//        this.senderId = senderId;
//        this.senderName = senderName;
//        this.profileImageUrl = profileImageUrl;
//        this.chatRoomId = chatRoomId;
//        this.message = message;
//        this.unreadCount = unreadCount;
//        this.messageType = messageType;
//        this.lastReadAt = lastReadAt;
//    }
//
//    public static ChatMessage of(ChatMessageRequest request, long chatRoomId, long senderId, String senderName, long unreadCount, String profileImageUrl) {
//        return ChatMessage.builder()
//                .senderId(senderId)
//                .senderName(senderName)
//                .chatRoomId(chatRoomId)
//                .message(request.message())
//                .profileImageUrl(profileImageUrl)
//                .unreadCount(unreadCount)
//                .messageType(MessageType.MESSAGE)
//                .lastReadAt(LocalDateTime.now())
//                .build();
//    }
//
//    public static ChatMessage of(long chatRoomId, Long senderId, String senderName, String message, MessageType messageType) {
//        return ChatMessage.builder()
//                .senderId(senderId)
//                .senderName(senderName)
//                .chatRoomId(chatRoomId)
//                .message(message)
//                .messageType(messageType)
//                .unreadCount(0L)
//                .lastReadAt(LocalDateTime.now())
//                .build();
//    }
//}
