//package com.playus.twpservice.domain.chat.entity;
//
//import com.playus.twpservice.domain.common.data.BaseTimeEntity;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.SQLDelete;
//import org.hibernate.annotations.SQLRestriction;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@SQLDelete(sql = "UPDATE chat_participants SET is_deleted = true WHERE id = ?")
//@SQLRestriction("is_deleted = false")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "chat_participants")
//public class ChatParticipant extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotNull
//    private Long userId;
//
//    @ManyToOne
//    @JoinColumn(name = "chat_room_id")
//    private ChatRoom chatRoom;
//
//    @Column(updatable = false)
//    private LocalDateTime joinedAt;
//
//    private LocalDateTime lastReadAt;
//
//    private LocalDateTime disconnectedAt;
//
//    private boolean isDeleted;
//
//    @Builder
//    private ChatParticipant(Long userId, ChatRoom chatRoom) {
//        this.userId = userId;
//        this.chatRoom = chatRoom;
//        this.lastReadAt = LocalDateTime.now();
//        this.joinedAt = LocalDateTime.now();
//        this.isDeleted = false;
//    }
//
//    public static ChatParticipant of(ChatRoom chatRoom, Long userId) {
//        return ChatParticipant.builder()
//                .chatRoom(chatRoom)
//                .userId(userId)
//                .build();
//    }
//
//    public void reSubscribe() {
//        this.lastReadAt = LocalDateTime.now();
//    }
//
//    public void unsubscribe() {
//        this.lastReadAt = LocalDateTime.now();
//        this.disconnectedAt = LocalDateTime.now();
//    }
//
//    public void disconnect() {
//        this.lastReadAt = LocalDateTime.now();
//        this.disconnectedAt = LocalDateTime.now();
//    }
//}
