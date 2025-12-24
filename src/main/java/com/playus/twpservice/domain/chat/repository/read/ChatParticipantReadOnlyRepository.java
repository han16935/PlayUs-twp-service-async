//package com.playus.twpservice.domain.chat.repository.read;
//
//import com.playus.twpservice.domain.chat.document.ChatParticipantDocument;
//import com.playus.twpservice.domain.common.data.BaseMongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//
//import java.util.List;
//
//public interface ChatParticipantReadOnlyRepository extends BaseMongoRepository<ChatParticipantDocument, Long> {
//
//    @Query(value = "{'chat_room_id' : ?0, 'is_deleted' : false}")
//    List<ChatParticipantDocument> findAllByChatRoomId(long chatRoomId);
//
//    long countByChatRoomId(long chatRoomId);
//}
