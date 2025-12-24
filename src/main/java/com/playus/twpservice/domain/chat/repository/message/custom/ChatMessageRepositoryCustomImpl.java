//package com.playus.twpservice.domain.chat.repository.message.custom;
//
//import com.playus.twpservice.domain.chat.document.ChatMessage;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.data.util.Pair;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//
//@Repository
//public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {
//
//    private static final String COLLECTION = "chat_messages";
//    private static final String EMPTY_READ_MESSAGE = "NO_MESSAGES";
//
//    private final MongoTemplate chatMongoTemplate;
//
//    public ChatMessageRepositoryCustomImpl(@Qualifier("chatMongoTemplate") MongoTemplate chatMongoTemplate) {
//        this.chatMongoTemplate = chatMongoTemplate;
//    }
//
//    public Pair<String, String> updateUnreadCount(Long chatRoomId, LocalDateTime lastReadAt, Long senderId) {
//        Query query = new Query().addCriteria(buildCommonCriteria(chatRoomId, lastReadAt, senderId));
//
//        Update update = new Update().inc("unread_count", -1);
//
//        chatMongoTemplate.updateMulti(query, update, ChatMessage.class);
//
//        return getUpdatedMessageRange(chatRoomId, lastReadAt, senderId);
//    }
//
//    private Pair<String, String> getUpdatedMessageRange(Long chatRoomId, LocalDateTime lastReadAt, Long senderId) {
//        Query minQuery = new Query().addCriteria(buildCommonCriteria(chatRoomId, lastReadAt, senderId))
//                .with(Sort.by(Sort.Direction.ASC, "_id")).limit(1);
//
//        Query maxQuery = new Query().addCriteria(buildCommonCriteria(chatRoomId, lastReadAt, senderId))
//                .with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
//
//        ChatMessage minMessage = chatMongoTemplate.findOne(minQuery, ChatMessage.class, COLLECTION);
//        ChatMessage maxMessage = chatMongoTemplate.findOne(maxQuery, ChatMessage.class, COLLECTION);
//
//        String minId = (minMessage != null) ? minMessage.getId() : EMPTY_READ_MESSAGE;
//        String maxId = (maxMessage != null) ? maxMessage.getId() : EMPTY_READ_MESSAGE;
//
//        return Pair.of(minId, maxId);
//    }
//
//    private Criteria buildCommonCriteria(Long chatRoomId, LocalDateTime lastReadAt, Long senderId) {
//        return Criteria.where("chatRoomId").is(chatRoomId)
//                .and("last_read_at").gt(lastReadAt)
//                .and("sender_id").ne(senderId)
//                .and("unread_count").gt(0);
//    }
//}
