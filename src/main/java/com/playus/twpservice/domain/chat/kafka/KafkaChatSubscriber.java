//package com.playus.twpservice.domain.chat.kafka;
//
//import com.playus.twpservice.domain.chat.dto.request.ChattingMessage;
//import com.playus.twpservice.domain.chat.exception.common.SubscribeException;
//import com.playus.twpservice.domain.chat.exception.common.WebSocketException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.messaging.MessagingException;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.stereotype.Component;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class KafkaChatSubscriber {
//
//    public static final String CHAT_ROOM_PREFIX = "/sub/chat/room/";
//
//    private final SimpMessageSendingOperations simpMessageSendingOperations;
//
//    @KafkaListener(topics = "${spring.kafka.topics.chat-room}", groupId = "${spring.kafka.consumer.chat-group-id}", containerFactory = "chatMessageListenerFactory")
//    public void consumeChatMessage(@Payload ChattingMessage chattingMessage, Acknowledgment acknowledgment) {
//        try {
//            if (chattingMessage == null || chattingMessage.chatRoomId() == null) {
//                log.warn("🚨 유효하지 않은 채팅 메시지를 받았습니다: {}", chattingMessage);
//                acknowledgment.acknowledge();
//                return;
//            }
//
//            log.info("📤 Kafka 채팅 메시지 읽기 성공: {}", chattingMessage);
//
//            simpMessageSendingOperations.convertAndSend(CHAT_ROOM_PREFIX + chattingMessage.chatRoomId(), chattingMessage);
//            acknowledgment.acknowledge();
//        } catch (MessagingException e) {
//            throw new WebSocketException.CustomMessagingException("STOMP 메시지 전송에 실패했습니다: " + e.getMessage());
//        }  catch (Exception e) {
//            throw new SubscribeException.RedisSubscribeException("[Kafka] 메시지 전송에 실패했습니다: " + e.getMessage());
//        }
//    }
//}
