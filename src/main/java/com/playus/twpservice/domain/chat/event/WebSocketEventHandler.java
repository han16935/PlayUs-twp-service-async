//package com.playus.twpservice.domain.chat.event;
//
//import com.playus.twpservice.domain.chat.entity.ChatParticipant;
//import com.playus.twpservice.domain.chat.exception.entity.ChatParticipantException;
//import com.playus.twpservice.domain.chat.service.ChatParticipantService;
//import com.playus.twpservice.domain.chat.service.ChatRedisService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
//
//import static com.playus.twpservice.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;
//import static com.playus.twpservice.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_ROOM_ID;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class WebSocketEventHandler {
//
//    private final ChatParticipantService chatParticipantService;
//    private final ChatRedisService chatRedisService;
//
//    @EventListener
//    @Transactional
//    public void handleDisconnect(SessionDisconnectEvent event) {
//        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
//
//        try {
//            long roomId = (long) accessor.getSessionAttributes().get(CHAT_ROOM_ID);
//            long userId = (long) accessor.getSessionAttributes().get(CHAT_USER_ID);
//
//            ChatParticipant chatParticipant = chatParticipantService.find(roomId, userId);
//
//            if (chatRedisService.isActive(roomId, userId)) {
//                chatParticipant.disconnect();
//                chatRedisService.removeSubscribeMember(roomId, userId);
//            }
//        } catch (NullPointerException e) {
//            log.info("[handleDisconnect] 구독 정보가 존재하지 않습니다.");
//        } catch (ChatParticipantException.NotFoundException e) {
//            log.warn("[handleDisconnect] 이미 퇴장한 참여자 입니다.");
//        }
//    }
//
//    @EventListener
//    @Transactional
//    public void handleUnsubscribe(SessionUnsubscribeEvent event) {
//        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
//
//        long roomId = (long) accessor.getSessionAttributes().get(CHAT_ROOM_ID);
//        long userId = (long) accessor.getSessionAttributes().get(CHAT_USER_ID);
//
//        try {
//            ChatParticipant chatParticipant = chatParticipantService.find(roomId, userId);
//
//            if (chatRedisService.isActive(roomId, userId)) {
//                chatParticipant.unsubscribe();
//                chatRedisService.removeSubscribeMember(roomId, userId);
//            }
//        } catch (ChatParticipantException.NotFoundException e) {
//            log.warn("[handleUnsubscribe] 이미 퇴장한 참여자 입니다.");
//        }
//    }
//}
