//package com.playus.twpservice.domain.chat.stomp.strategy;
//
//import com.playus.twpservice.domain.chat.exception.common.WebSocketException;
//import com.playus.twpservice.global.jwt.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class StompConnectStrategy implements StompCommandStrategy {
//
//    private static final String ACCESS_COOKIE_NAME = "Access";
//    public static final String CHAT_USER_ID = "CHAT_USER_ID";
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public boolean supports(StompCommand command) {
//        return StompCommand.CONNECT.equals(command);
//    }
//
//    @Override
//    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
//        String jwtToken = null;
//        String cookieHeader = accessor.getFirstNativeHeader("Cookie");
//
//        if (cookieHeader != null) {
//            String[] cookies = cookieHeader.split(";\\s*");
//
//            for (String cookie : cookies) {
//                String[] cookieParts = cookie.split("=", 2);
//
//                if (cookieParts.length == 2 && ACCESS_COOKIE_NAME.equals(cookieParts[0].trim())) {
//                    jwtToken = cookieParts[1].trim();
//                    break;
//                }
//            }
//        }
//
//        if (jwtToken == null) {
//            throw new WebSocketException.TokenNotExistException("쿠키에 인증 토큰이 존재하지 않습니다");
//        }
//
//        if (jwtUtil.isExpired(jwtToken)) {
//            throw new WebSocketException.TokenExpiredException("만료된 토큰입니다");
//        }
//
//        Long userId = Long.parseLong(jwtUtil.getUserId(jwtToken));
//        accessor.getSessionAttributes().put(CHAT_USER_ID, userId);
//
//        return message;
//    }
//}
