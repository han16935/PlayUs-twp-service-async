//package com.playus.twpservice.domain.chat.service;
//
//import com.playus.twpservice.domain.chat.dto.request.ChatMessageRequest;
//import com.playus.twpservice.domain.chat.dto.request.ChattingMessage;
//import com.playus.twpservice.domain.chat.dto.response.ChatPageableResponse;
//import com.playus.twpservice.domain.chat.dto.response.ChatResponse;
//import com.playus.twpservice.domain.chat.dto.response.ChattingMessageResponse;
//import com.playus.twpservice.domain.chat.document.ChatMessage;
//import com.playus.twpservice.domain.chat.entity.ChatParticipant;
//import com.playus.twpservice.domain.chat.entity.ChatRoom;
//import com.playus.twpservice.domain.chat.exception.common.WebSocketException;
//import com.playus.twpservice.domain.chat.repository.message.ChatMessageRepository;
//import com.playus.twpservice.domain.chat.kafka.KafkaChatPublisher;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.data.domain.Sort;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static com.playus.twpservice.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;
//import static com.playus.twpservice.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_ROOM_ID;
//import static com.playus.twpservice.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_USER_NAME;
//
//@Service
//@RequiredArgsConstructor
//public class ChattingService {
//
//    private final ChatMessageRepository chatMessageRepository;
//    private final KafkaChatPublisher kafkaChatPublisher;
//    private final ChatRoomService chatRoomService;
//    private final ChatParticipantService chatParticipantService;
//    private final ChatRedisService chatRedisService;
//
//    @Transactional
//    public void chat(ChatMessageRequest request, SimpMessageHeaderAccessor accessor) {
//        long roomId = getSessionAttribute(accessor, CHAT_ROOM_ID, Long.class);
//        long userId =  getSessionAttribute(accessor, CHAT_USER_ID, Long.class);
//        String senderName = getSessionAttribute(accessor, CHAT_USER_NAME, String.class);
//
//        long unreadCount = getUnreadCount(roomId);
//        String profileImageUrl = chatRedisService.getProfileImageUrl(roomId, userId);
//
//        ChatMessage chatMessage = ChatMessage.of(request, roomId, userId, senderName, unreadCount, profileImageUrl);
//
//        chatMessageRepository.save(chatMessage);
//
//        ChattingMessage chattingMessage = ChattingMessage.from(chatMessage);
//
//        kafkaChatPublisher.publish(chattingMessage);
//    }
//
//    public ChatResponse getMessage(long roomId, long senderId, int pageNumber, int pageSize, LocalDateTime lastMessageTimeStamp) {
//        ChatRoom chatRoom = chatRoomService.find(roomId);
//        ChatParticipant chatParticipant = chatParticipantService.find(chatRoom, senderId);
//
//        chatRedisService.checkSubscriptionStatus(roomId, senderId);
//
//        Slice<ChatMessage> chatMessages = loadMessage(roomId, chatParticipant, pageNumber, pageSize, lastMessageTimeStamp);
//
//        return getChatResponse(pageNumber, chatMessages, chatParticipant);
//    }
//
//    private ChatResponse getChatResponse(int pageNumber, Slice<ChatMessage> chatMessages, ChatParticipant chatParticipant) {
//        List<ChattingMessageResponse> chattingMessageResponses = chatMessages.stream()
//                .map(ChattingMessageResponse::from)
//                .toList();
//
//        ChatPageableResponse chatPageableResponse = ChatPageableResponse.of(pageNumber, chatMessages);
//
//        return ChatResponse.of(chatParticipant, chattingMessageResponses, chatPageableResponse);
//    }
//
//    private Slice<ChatMessage> loadMessage(long roomId, ChatParticipant chatParticipant, int pageNumber, int pageSize, LocalDateTime lastMessageTimeStamp) {
//        if (pageNumber == 0) {
//            return loadInitialMessage(roomId, chatParticipant, pageSize);
//        }
//
//        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "lastReadAt"));
//        return chatMessageRepository.findAllByRoomIdAndLastReadAtAfterAndLastReadAtBeforeOrderByLastReadAtDesc(roomId, chatParticipant.getJoinedAt(), lastMessageTimeStamp, pageable);
//    }
//
//    private Slice<ChatMessage> loadInitialMessage(long roomId, ChatParticipant chatParticipant, int pageSize) {
//        int chattingCount = chatMessageRepository.countAllByChatRoomIdAndLastReadAtAfterOrderByLastReadAtDesc(roomId, chatParticipant.getDisconnectedAt());
//
//        int effectivePageSize = Math.max(chattingCount, pageSize);
//        Pageable pageable = PageRequest.of(0, effectivePageSize, Sort.by(Sort.Direction.DESC, "lastReadAt"));
//
//        return chatMessageRepository.findAllByChatRoomIdAndLastReadAtAfterOrderByLastReadAtDesc(roomId, chatParticipant.getJoinedAt(), pageable);
//    }
//
//    private <T> T getSessionAttribute(SimpMessageHeaderAccessor accessor, String attributeName, Class<T> type) {
//        return Optional.ofNullable(accessor.getSessionAttributes())
//                .map(attrs -> type.cast(attrs.get(attributeName)))
//                .orElseThrow(() -> new WebSocketException.WebSocketSessionException(attributeName + " 가 웹소켓 세션에 존재하지 않습니다."));
//    }
//
//    private long getUnreadCount(long roomId) {
//        long totalCount = chatParticipantService.getParticipantCount(roomId);
//        long nowCount = chatRedisService.getSubscriberCount(roomId);
//
//        return Math.max(0, totalCount - nowCount);
//    }
//}
