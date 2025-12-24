//package com.playus.twpservice.global.exception;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.playus.twpservice.global.response.ErrorResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
//
//import java.nio.charset.StandardCharsets;
//
//@Component
//@RequiredArgsConstructor
//public class StompExceptionHandler extends StompSubProtocolErrorHandler {
//
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
//        Throwable cause = ex.getCause();
//
//        if (cause instanceof RuntimeException runtimeException) {
//            return sendErrorMessage(runtimeException);
//        }
//
//        return super.handleClientMessageProcessingError(clientMessage, ex);
//    }
//
//    private Message<byte[]> sendErrorMessage(RuntimeException ex) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
//
//        accessor.setHeader("content-type", "application/json");
//        accessor.setMessage(ex.getMessage());
//        accessor.setLeaveMutable(true);
//
//        ErrorResponse errorResponse = ErrorResponse.badRequestError(ex.getMessage());
//
//        String payload;
//
//        try {
//            payload = objectMapper.writeValueAsString(errorResponse);
//        } catch (JsonProcessingException e) {
//            payload = "{\"error\": \"" + ex.getMessage() + "\"}";
//        }
//
//        return MessageBuilder.createMessage(
//                payload.getBytes(StandardCharsets.UTF_8),
//                accessor.getMessageHeaders()
//        );
//    }
//}
