//package com.playus.twpservice.domain.chat.stomp.strategy;
//
//import com.playus.twpservice.domain.chat.exception.entity.ChatMessageException;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class StompSendStrategy implements StompCommandStrategy {
//
//    private static final String SEND_END_POINT = "/pub/chat/message";
//
//    @Override
//    public boolean supports(StompCommand command) {
//        return StompCommand.SEND.equals(command);
//    }
//
//    @Override
//    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
//        String destination = accessor.getDestination();
//
//        if (!destination.startsWith(SEND_END_POINT)) {
//            throw new ChatMessageException.ChatSendEndPointException("올바르지 않은 채팅 메시지 경로입니다.");
//        }
//
//        return message;
//    }
//}
