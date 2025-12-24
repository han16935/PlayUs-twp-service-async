//package com.playus.twpservice.domain.chat.service;
//
//import com.playus.twpservice.domain.chat.exception.common.SubscribeException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class ChatRedisService {
//    public static final String REDIS_CHAT_KEY_PREFIX = "ROOM:";
//
//    private final RedisTemplate<String, Object> chatRoomRedisTemplate;
//
//    public void saveSubscribeMember(long roomId, long senderId, String profilePicture) {
//        String key = getKey(roomId);
//
//        chatRoomRedisTemplate.opsForHash().put(key, String.valueOf(senderId), profilePicture);
//    }
//
//    public boolean isActive(long roomId, long senderId) {
//        String key = getKey(roomId);
//
//        return Boolean.TRUE.equals(chatRoomRedisTemplate.opsForHash().hasKey(key, String.valueOf(senderId)));
//    }
//
//    public void removeSubscribeMember(long roomId, long senderId) {
//        String key = getKey(roomId);
//
//        chatRoomRedisTemplate.opsForHash().delete(key, String.valueOf(senderId));
//    }
//
//    public void checkSubscriptionStatus(long roomId, long senderId) {
//        if (!isActive(roomId, senderId)) {
//            throw new SubscribeException.UnSubscriptionException("채팅방을 구독하지 않은 참여자 입니다.");
//        }
//    }
//
//    public long getSubscriberCount(long roomId) {
//        String key = getKey(roomId);
//        try {
//            return chatRoomRedisTemplate.opsForHash().size(key);
//        } catch (Exception e) {
//            throw new SubscribeException.RedisSubscribeException("채팅방 구독자 수 조회 중 오류가 발생하였습니다.");
//        }
//    }
//
//    public String getProfileImageUrl(long roomId, long senderId) {
//        String key = getKey(roomId);
//
//        return Optional.ofNullable(chatRoomRedisTemplate.opsForHash().get(key, String.valueOf(senderId)))
//                .map(Object::toString)
//                .orElse(null);
//    }
//
//    private String getKey(long roomId) {
//        return REDIS_CHAT_KEY_PREFIX + roomId;
//    }
//}
