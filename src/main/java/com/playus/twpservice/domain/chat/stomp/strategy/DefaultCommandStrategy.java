//package com.playus.twpservice.domain.chat.stomp.strategy;
//
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DefaultCommandStrategy implements StompCommandStrategy{
//
//    @Override
//    public boolean supports(StompCommand command) {
//        return false;
//    }
//
//    @Override
//    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
//        return message;
//    }
//}
