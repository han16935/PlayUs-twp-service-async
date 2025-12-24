//package com.playus.twpservice.domain.chat.document;
//
//import com.playus.twpservice.domain.chat.entity.enums.ChatStatus;
//import jakarta.validation.constraints.NotNull;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@Document(collection = "chat_rooms")
//public class ChatRoomDocument {
//
//    @Id
//    private Long id;
//
//    @NotNull
//    private ChatStatus status;
//
//    @Builder
//    private ChatRoomDocument(Long id, ChatStatus status) {
//        this.id = id;
//        this.status = status;
//    }
//
//    public static ChatRoomDocument createForOnlyTest(Long id, ChatStatus status) {
//        return ChatRoomDocument.builder()
//                .id(id)
//                .status(status)
//                .build();
//    }
//}
