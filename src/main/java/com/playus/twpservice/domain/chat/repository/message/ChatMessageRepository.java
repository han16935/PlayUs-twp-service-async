//package com.playus.twpservice.domain.chat.repository.message;
//
//import com.playus.twpservice.domain.chat.document.ChatMessage;
//import com.playus.twpservice.domain.common.data.BaseMongoRepository;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.data.mongodb.repository.Query;
//
//import java.time.LocalDateTime;
//
//public interface ChatMessageRepository extends BaseMongoRepository<ChatMessage, String> {
//
//
//    void deleteAllByChatRoomId(Long chatRoomId);
//
//    Slice<ChatMessage> findAllByChatRoomIdAndLastReadAtAfterOrderByLastReadAtDesc(Long chatRoomId, LocalDateTime joinedAt, Pageable pageable);
//
//    @Query("""
//            {
//                'chat_room_id': ?0,
//                'last_read_at': {
//                    $gt: ?1,
//                    $lt: ?2
//                }
//            }
//            """)
//    Slice<ChatMessage> findAllByRoomIdAndLastReadAtAfterAndLastReadAtBeforeOrderByLastReadAtDesc(Long chatRoomId, LocalDateTime joinedAt, LocalDateTime lastMessageTimeStamp, Pageable pageable);
//
//    Integer countAllByChatRoomIdAndLastReadAtAfterOrderByLastReadAtDesc(Long chatRoomId, LocalDateTime lastReadAt);
//}
