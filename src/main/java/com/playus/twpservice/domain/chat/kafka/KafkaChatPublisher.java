//package com.playus.twpservice.domain.chat.kafka;
//
//import com.playus.twpservice.domain.chat.dto.request.ChattingMessage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class KafkaChatPublisher {
//
//    private final KafkaTemplate<String, ChattingMessage> chatKafkaTemplate;
//
//    @Value("${spring.kafka.topics.chat-room}")
//    private String topic;
//
//    public void publish(ChattingMessage chattingMessage) {
//        log.info("📤 Kafka 채팅 메시지 발행: {}", chattingMessage);
//        chatKafkaTemplate.send(topic, chattingMessage.chatRoomId().toString(), chattingMessage);
//    }
//}
