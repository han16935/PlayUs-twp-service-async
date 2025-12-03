//package com.playus.twpservice.domain.chat.service;
//
//import com.playus.twpservice.domain.chat.dto.request.ChattingMessage;
//import com.playus.twpservice.domain.chat.dto.response.ReadMessageRange;
//import com.playus.twpservice.domain.chat.entity.ChatParticipant;
//import com.playus.twpservice.domain.chat.entity.ChatRoom;
//import com.playus.twpservice.domain.chat.entity.enums.MessageType;
//import com.playus.twpservice.domain.chat.exception.common.SubscribeException;
//import com.playus.twpservice.domain.chat.exception.entity.ChatParticipantException;
//import com.playus.twpservice.domain.chat.repository.read.ChatParticipantReadOnlyRepository;
//import com.playus.twpservice.domain.chat.repository.write.ChatParticipantRepository;
//import com.playus.twpservice.domain.chat.repository.message.custom.ChatMessageRepositoryCustom;
//import com.playus.twpservice.domain.common.feign.client.UserFeignClient;
//import com.playus.twpservice.domain.chat.kafka.KafkaChatPublisher;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.util.Pair;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class ChatParticipantService {
//
//    private final ChatParticipantRepository chatParticipantRepository;
//    private final ChatParticipantReadOnlyRepository chatParticipantReadOnlyRepository;
//    private final ChatMessageRepositoryCustom chatMessageRepository;
//    private final ChatRedisService chatRedisService;
//    private final KafkaChatPublisher kafkaChatPublisher;
//    private final UserFeignClient userFeignClient;
//
//    public void save(ChatParticipant chatParticipant) {
//        chatParticipantRepository.save(chatParticipant);
//    }
//
//    public ChatParticipant find(ChatRoom chatRoom, Long userId) {
//        return chatParticipantRepository.findByChatRoomAndUserId(chatRoom, userId)
//                .orElseThrow(() -> new ChatParticipantException.NotFoundException("존재하지 않는 채팅 참여자 입니다."));
//    }
//
//    public ChatParticipant find(long roomId, long memberId) {
//        return chatParticipantRepository.findByChatRoomIdAndUserId(roomId, memberId)
//                .orElseThrow(() -> new ChatParticipantException.NotFoundException("존재하지 않는 채팅 참여자 입니다."));
//    }
//
//    @Transactional
//    public boolean checkSubscription(ChatRoom chatRoom, Long userId) {
//        Optional<ChatParticipant> optionalParticipant = chatParticipantRepository.findByChatRoomAndUserId(chatRoom, userId);
//
//        if (optionalParticipant.isPresent()) {
//            ChatParticipant chatParticipant = optionalParticipant.get();
//
//            checkDuplicateSubscription(chatRoom.getId(), userId);
//
//            ReadMessageRange messageRange = updateUnreadMessages(chatRoom.getId(), chatParticipant.getLastReadAt(), userId);
//
//            String nickname = userFeignClient.getUserInfo(userId).nickname();
//
//            reEnterEvent(chatRoom.getId(), userId, nickname, messageRange);
//
//            chatParticipant.reSubscribe();
//
//            return true;
//        }
//
//        return false;
//    }
//
//    public void delete(ChatParticipant chattingParticipant) {
//        chatParticipantRepository.delete(chattingParticipant);
//    }
//
//    public long getParticipantCount(Long chatRoomId) {
//        return chatParticipantReadOnlyRepository.countByChatRoomId(chatRoomId);
//    }
//
//    private void checkDuplicateSubscription(long roomId, long memberId) {
//        if (chatRedisService.isActive(roomId, memberId)) {
//            throw new SubscribeException.DuplicateSubscribeException("같은 채팅방을 중복으로 구독했습니다.");
//        }
//    }
//
//    private ReadMessageRange updateUnreadMessages(Long roomId, LocalDateTime lastReadAt, Long userId) {
//        Pair<String, String> pair = chatMessageRepository.updateUnreadCount(roomId, lastReadAt, userId);
//        return ReadMessageRange.from(pair);
//    }
//
//    private void reEnterEvent(long chatRoomId, long senderId, String senderName, ReadMessageRange range) {
//        ChattingMessage chattingMessage = ChattingMessage.of(chatRoomId, senderId, senderName, range, MessageType.READ);
//
//        kafkaChatPublisher.publish(chattingMessage);
//    }
//}
