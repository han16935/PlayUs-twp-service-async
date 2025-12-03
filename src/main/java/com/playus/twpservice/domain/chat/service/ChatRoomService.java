//package com.playus.twpservice.domain.chat.service;
//
//import com.playus.twpservice.domain.chat.document.ChatParticipantDocument;
//import com.playus.twpservice.domain.chat.dto.request.ChattingMessage;
//import com.playus.twpservice.domain.chat.dto.response.ChatUserResponse;
//import com.playus.twpservice.domain.chat.dto.response.ChatUserInfoResponse;
//import com.playus.twpservice.domain.chat.dto.response.ReadMessageRange;
//import com.playus.twpservice.domain.chat.document.ChatMessage;
//import com.playus.twpservice.domain.chat.entity.ChatParticipant;
//import com.playus.twpservice.domain.chat.entity.ChatRoom;
//import com.playus.twpservice.domain.chat.entity.enums.ChatStatus;
//import com.playus.twpservice.domain.chat.entity.enums.MessageType;
//import com.playus.twpservice.domain.chat.exception.entity.ChatRoomException;
//import com.playus.twpservice.domain.chat.repository.message.ChatMessageRepository;
//import com.playus.twpservice.domain.chat.repository.read.ChatParticipantReadOnlyRepository;
//import com.playus.twpservice.domain.chat.repository.write.ChatRoomRepository;
//import com.playus.twpservice.domain.chat.repository.message.custom.ChatMessageRepositoryCustom;
//import com.playus.twpservice.domain.party.entity.Party;
//import com.playus.twpservice.domain.party.exception.entity.PartyException;
//import com.playus.twpservice.domain.common.feign.client.UserFeignClient;
//import com.playus.twpservice.domain.common.feign.response.UserInfoResponse;
//import com.playus.twpservice.domain.chat.kafka.KafkaChatPublisher;
//import com.playus.twpservice.domain.party.repository.write.PartyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.util.Pair;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static com.playus.twpservice.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;
//import static com.playus.twpservice.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_ROOM_ID;
//import static com.playus.twpservice.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_USER_NAME;
//
//@Service
//@RequiredArgsConstructor
//public class ChatRoomService {
//
//    private static final String ENTER_MESSAGE = " 님이 입장하셨습니다.";
//    private static final String EXIT_MESSAGE = " 님이 퇴장하셨습니다.";
//
//    private final ChatRoomRepository chatRoomRepository;
//    private final ChatMessageRepository chatMessageRepository;
//    private final PartyRepository partyRepository;
//    private final ChatMessageRepositoryCustom chatMessageRepositoryCustom;
//    private final ChatParticipantService chatParticipantService;
//    private final ChatParticipantReadOnlyRepository chatParticipantReadOnlyRepository;
//    private final KafkaChatPublisher kafkaChatPublisher;
//    private final UserFeignClient userFeignClient;
//    private final ChatRedisService chatRedisService;
//
//    @Transactional
//    public ChatRoom create() {
//        ChatRoom chatRoom = ChatRoom.create();
//
//        return chatRoomRepository.save(chatRoom);
//    }
//
//    @Transactional
//    public void delete(long chatRoomId) {
//        chatRoomRepository.deleteById(chatRoomId);
//    }
//
//    public ChatUserInfoResponse getCount(Long userId, Long chatRoomId) {
//        chatParticipantService.find(chatRoomId, userId);
//
//        Party party = partyRepository.findByChatRoomId(chatRoomId)
//                .orElseThrow(() -> new PartyException.NotFoundException("해당 채팅방에 대한 파티가 존재하지 않습니다."));
//
//        Long writerId = party.getWriterId();
//        UserInfoResponse writer = userFeignClient.getUserInfo(writerId);
//        ChatUserResponse writerInfo = ChatUserResponse.of(writerId, writer);
//
//        List<ChatParticipantDocument> chatParticipants = chatParticipantReadOnlyRepository.findAllByChatRoomId(chatRoomId);
//        List<ChatUserResponse> participants = chatParticipants
//                .parallelStream()
//                .map(participant ->
//                        ChatUserResponse.of(participant.getUserId(), userFeignClient.getUserInfo(participant.getUserId())))
//                .toList();
//
//        long count = chatParticipants.size();
//
//        return ChatUserInfoResponse.of(chatRoomId, count, writerInfo, participants);
//    }
//
//    @Transactional
//    public void postSubscribeChatroom(SimpMessageHeaderAccessor accessor) {
//        long roomId = (long) accessor.getSessionAttributes().get(CHAT_ROOM_ID);
//        long senderId = (long) accessor.getSessionAttributes().get(CHAT_USER_ID);
//
//        ChatRoom chatRoom = find(roomId);
//        UserInfoResponse userInfo = userFeignClient.getUserInfo(senderId);
//
//        accessor.getSessionAttributes().put(CHAT_USER_NAME, userInfo.nickname());
//
//        chatRedisService.saveSubscribeMember(roomId, senderId, userInfo.profileImageUrl());
//
//        if (chatParticipantService.checkSubscription(chatRoom, senderId)) {
//            return;
//        }
//
//        ChatParticipant newParticipant = ChatParticipant.of(chatRoom, senderId);
//        chatParticipantService.save(newParticipant);
//
//        publishMessage(roomId, senderId, userInfo.nickname(), ENTER_MESSAGE, MessageType.ENTER);
//    }
//
//    @Transactional
//    public void exitChatRoom(long roomId, long senderId) {
//        ChatRoom chatRoom = find(roomId);
//        UserInfoResponse userInfo = userFeignClient.getUserInfo(senderId);
//        ChatParticipant chatParticipant = chatParticipantService.find(chatRoom, senderId);
//
//        chatParticipantService.delete(chatParticipant);
//
//        Pair<String, String> pair = chatMessageRepositoryCustom.updateUnreadCount(chatRoom.getId(), chatParticipant.getLastReadAt(), senderId);
//
//        publishMessage(roomId, senderId, userInfo.nickname(), EXIT_MESSAGE, MessageType.EXIT, ReadMessageRange.from(pair));
//    }
//
//    public ChatRoom find(long chatRoomId) {
//        return chatRoomRepository.findById(chatRoomId)
//                .filter(chatRoom -> chatRoom.getStatus() == ChatStatus.ACTIVE)
//                .orElseThrow(() -> new ChatRoomException.NotFoundException("채팅방이 존재하지 않습니다!"));
//    }
//
//    private void publishMessage(long roomId, long senderId, String senderName, String message, MessageType messageType) {
//        ChatMessage chatMessage = ChatMessage.of(roomId, senderId, senderName, senderName + message, messageType);
//        chatMessageRepository.save(chatMessage);
//        ChattingMessage chattingMessage = ChattingMessage.from(chatMessage);
//
//        kafkaChatPublisher.publish(chattingMessage);
//    }
//
//    private void publishMessage(long roomId, long senderId, String senderName, String message, MessageType messageType, ReadMessageRange range) {
//        ChatMessage chatMessage = ChatMessage.of(roomId, senderId, senderName, senderName + message, messageType);
//        chatMessageRepository.save(chatMessage);
//        ChattingMessage chattingMessage = ChattingMessage.of(roomId, senderId, senderName, range, messageType);
//
//        kafkaChatPublisher.publish(chattingMessage);
//    }
//}
