//package com.playus.twpservice.domain.chat.repository;
//
//import com.playus.twpservice.IntegrationTestSupport;
//import com.playus.twpservice.domain.chat.entity.ChatParticipant;
//import com.playus.twpservice.domain.chat.entity.ChatRoom;
//import com.playus.twpservice.domain.chat.repository.message.ChatMessageRepository;
//import com.playus.twpservice.domain.chat.repository.write.ChatParticipantRepository;
//import com.playus.twpservice.domain.chat.repository.write.ChatRoomRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Transactional
//class ChatParticipantRepositoryTest extends IntegrationTestSupport {
//
//    @Autowired
//    ChatParticipantRepository chatParticipantRepository;
//
//    @Autowired
//    ChatRoomRepository chatRoomRepository;
//
//    @Autowired
//    ChatMessageRepository chatMessageRepository;
//
//    @AfterEach
//    void tearDown() {
//        chatMessageRepository.deleteAll();
//
//        chatParticipantRepository.deleteAll();
//        chatRoomRepository.deleteAll();
//    }
//
//    @DisplayName("사용자 ID에 따라 참여중인 채팅방 목록을 찾을 수 있다.")
//    @Test
//    void findAllByUserId() {
//        // given
//        Long userId = 1L;
//        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create());
//        ChatRoom chatRoom1 = chatRoomRepository.save(ChatRoom.create());
//
//        chatParticipantRepository.saveAll(List.of(
//                ChatParticipant.of(chatRoom, userId),
//                ChatParticipant.of(chatRoom1, userId)
//        ));
//
//        // when
//        List<ChatParticipant> result = chatParticipantRepository.findAllByUserId(userId);
//
//        // then
//        assertThat(result).hasSize(2);
//    }
//
//}
