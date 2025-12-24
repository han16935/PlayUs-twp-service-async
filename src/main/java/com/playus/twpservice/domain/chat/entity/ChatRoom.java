//package com.playus.twpservice.domain.chat.entity;
//
//import com.playus.twpservice.domain.chat.entity.enums.ChatStatus;
//import com.playus.twpservice.domain.common.data.BaseTimeEntity;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.SQLDelete;
//import org.hibernate.annotations.SQLRestriction;
//
//@Getter
//@Entity
//@SQLDelete(sql = "UPDATE chat_rooms SET status = 'INACTIVE' WHERE id = ?")
//@SQLRestriction("status != 'INACTIVE'")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "chat_rooms")
//public class ChatRoom extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    private ChatStatus status;
//
//    @Builder
//    private ChatRoom (ChatStatus status) {
//        this.status = status;
//    }
//
//    public static ChatRoom create() {
//        return ChatRoom.builder()
//                .status(ChatStatus.ACTIVE)
//                .build();
//    }
//}
