//package com.playus.twpservice.domain.chat.stomp.strategy;
//
//import com.playus.twpservice.domain.chat.exception.stomp.StompException;
//import com.playus.twpservice.domain.chat.service.ChatRoomService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class StompSubscribeStrategy implements StompCommandStrategy {
//
//    public static final String CHAT_ROOM_ID = "CHAT_ROOM_ID";
//    public static final String CHAT_USER_NAME = "CHAT_USER_NAME";
//    private static final String SUB_END_POINT = "/sub/chat/room/";
//    private static final String ERROR_END_POINT = "/user/queue/errors";
//    private final ChatRoomService chatRoomService;
//
//    @Override
//    public boolean supports(StompCommand command) {
//        return StompCommand.SUBSCRIBE.equals(command);
//    }
//
//    @Override
//    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
//        String destination = accessor.getDestination();
//
//        if (destination == null) {
//            throw new StompException.ChatSubscribeException("구독 경로가 존재하지 않습니다.");
//        }
//
//        if (destination.startsWith(SUB_END_POINT)) {
//            String roomIdStr = destination.replace(SUB_END_POINT, "");
//            try {
//                Long roomId = Long.valueOf(roomIdStr);
//                accessor.getSessionAttributes().put(CHAT_ROOM_ID, roomId);
//            } catch (NumberFormatException e) {
//                throw new StompException.ChatSubscribeException("올바르지 않은 채팅방 ID입니다: " + roomIdStr);
//            } catch (NullPointerException e) {
//                throw new StompException.ChatSubscribeException("채팅방 ID가 존재하지 않습니다.");
//            }
//
//            return message;
//        }
//
//        if (destination.startsWith(ERROR_END_POINT)) {
//            return message;
//        }
//
//        throw new StompException.ChatSubscribeException("올바르지 않은 채팅 구독 경로입니다.");
//    }
//
//    @Override
//    public void postSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel, boolean sent) {
//        if (sent) {
//            chatRoomService.postSubscribeChatroom(accessor);
//        }
//    }
//}
//
