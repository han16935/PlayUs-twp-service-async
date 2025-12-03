//package com.playus.twpservice.domain.chat.document;
//
//import jakarta.persistence.Id;
//import jakarta.validation.constraints.NotNull;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//
//import java.time.LocalDateTime;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@Document(collection = "chat_participants")
//public class ChatParticipantDocument {
//
//    @Id
//    private Long id;
//
//    @NotNull
//    @Field(name = "user_id")
//    private Long userId;
//
//    @NotNull
//    @Field(name = "chat_room_id")
//    private Long chatRoomId;
//
//    @NotNull
//    @Field(name = "joined_at")
//    private LocalDateTime joinedAt;
//
//    @NotNull
//    @Field(name = "last_read_at")
//    private LocalDateTime lastReadAt;
//
//    @Field(name = "disconnected_at")
//    private LocalDateTime disconnectedAt;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//    @Builder
//    private ChatParticipantDocument(Long id, Long userId, Long chatRoomId, LocalDateTime joinedAt, LocalDateTime lastReadAt, LocalDateTime disconnectedAt) {
//        this.id = id;
//        this.userId = userId;
//        this.chatRoomId = chatRoomId;
//        this.joinedAt = joinedAt;
//        this.lastReadAt = lastReadAt;
//        this.disconnectedAt = disconnectedAt;
//    }
//
//    public static ChatParticipantDocument createForOnlyTest(Long id, Long userId, Long chatRoomId, LocalDateTime joinedAt, LocalDateTime lastReadAt, LocalDateTime disconnectedAt) {
//        return ChatParticipantDocument.builder()
//                .id(id)
//                .userId(userId)
//                .chatRoomId(chatRoomId)
//                .joinedAt(joinedAt)
//                .lastReadAt(lastReadAt)
//                .disconnectedAt(disconnectedAt)
//                .build();
//    }
//}
