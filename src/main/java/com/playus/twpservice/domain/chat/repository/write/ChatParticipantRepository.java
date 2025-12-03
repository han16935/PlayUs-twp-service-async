//package com.playus.twpservice.domain.chat.repository.write;
//
//import com.playus.twpservice.domain.chat.entity.ChatParticipant;
//import com.playus.twpservice.domain.chat.entity.ChatRoom;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
//
//    @Modifying
//    void deleteByChatRoomId(long chatRoomId);
//
//    Optional<ChatParticipant> findByChatRoomAndUserId(ChatRoom chatRoom, Long userId);
//
//    Optional<ChatParticipant> findByChatRoomIdAndUserId(long chatRoomId, long userId);
//
//    List<ChatParticipant> findAllByUserId(long userId);
//}
