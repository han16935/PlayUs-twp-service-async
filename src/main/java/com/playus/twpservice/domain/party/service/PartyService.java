package com.playus.twpservice.domain.party.service;

import com.playus.twpservice.domain.chat.entity.ChatParticipant;
import com.playus.twpservice.domain.chat.entity.ChatRoom;
import com.playus.twpservice.domain.chat.repository.message.ChatMessageRepository;
import com.playus.twpservice.domain.chat.repository.write.ChatParticipantRepository;
import com.playus.twpservice.domain.chat.repository.write.ChatRoomRepository;
import com.playus.twpservice.domain.chat.service.ChatRoomService;
import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.common.security.Gender;
import com.playus.twpservice.domain.party.assertion.PartyAssert;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.dto.apply.PartyApplyResponse;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveResponse;
import com.playus.twpservice.domain.party.dto.cancel.PartyCancelResponse;
import com.playus.twpservice.domain.party.dto.create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.create.PartyCreateResponse;
import com.playus.twpservice.domain.party.dto.delete.PartyDeleteResponse;
import com.playus.twpservice.domain.common.request.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.end.PartyEndResponse;
import com.playus.twpservice.domain.party.dto.leave.PartyLeaveResponse;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.entity.PartyAge;
import com.playus.twpservice.domain.party.entity.PartyJoin;
import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import com.playus.twpservice.domain.party.enums.PartyJoinMethod;
import com.playus.twpservice.domain.party.enums.PartyJoinRequestStatus;
import com.playus.twpservice.domain.party.exception.entity.PartyException;
import com.playus.twpservice.domain.common.feign.client.NotificationFeignClient;
import com.playus.twpservice.domain.common.feign.event.PartyNotificationEvent;
import com.playus.twpservice.domain.party.repository.read.PartyAgeReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyJoinReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.read.PartyReadOnlyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyAgeRepository;
import com.playus.twpservice.domain.party.repository.write.PartyJoinRepository;
import com.playus.twpservice.domain.party.repository.write.PartyRepository;
import com.playus.twpservice.domain.party.repository.write.PartyThumbnailUrlRepository;
//import com.playus.twpservice.global.s3.S3Service;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.playus.twpservice.domain.party.exception.entity.PartyException.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyJoinRepository partyJoinRepository;
    private final PartyAgeRepository partyAgeRepository;
    private final PartyThumbnailUrlRepository partyThumbnailUrlRepository;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    private final PartyReadOnlyRepository partyReadOnlyRepository;
    private final PartyJoinReadOnlyRepository partyJoinReadOnlyRepository;
    private final PartyAgeReadOnlyRepository partyAgeReadOnlyRepository;

    private final NotificationFeignClient notificationFeignClient;
//    private final S3Service s3Service;

    public PartyCreateResponse createParty(Long userId, PartyCreateRequest request) {
        ChatRoom chatRoom = initializeChatRoomAsWriter(userId);

        if (partyReadOnlyRepository.existsByWriterId(userId)) {
            throw new AlreadyCreatedPartyForPerMatchException("하나의 경기에 대해 하나의 직관팟만 만들 수 있습니다!");
        }

        Party party = partyRepository.save(request.toPartyWith(userId, chatRoom));

        List<PartyAge> partyAgeList = toPartyAgeEntity(request, party);
        partyAgeRepository.saveAll(partyAgeList);

        saveThumbnailUrlIfPresent(request, party);

        return PartyCreateResponse.of(party.getId(), chatRoom.getId());
    }

    public PartyUpdateResponse updateParty(Long userId, PartyIdRequest idRequest, PartyUpdateRequest updateRequest) {
        Long partyId = idRequest.partyId();
        Long writerId = updateRequest.writerId();

        PartyAssert.isLoginUserWriter(userId, writerId, "직관팟 작성자가 아니면 수정할 수 없습니다!");

        Party savedParty = partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("잘못된 직관팟 번호입니다!"));

        savedParty.updateParty(updateRequest);

        updatePartyThumbnails(updateRequest, partyId, savedParty);
        updatePartyAgeGroup(updateRequest, partyId, savedParty);

        return PartyUpdateResponse.of(partyId);
    }

    public PartyDeleteResponse deleteParty(Long userId, Long partyId) {

        PartyDocument partyDocument = partyReadOnlyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("잘못된 직관팟 번호입니다!"));

        PartyAssert.isLoginUserWriter(userId, partyDocument.getWriterId(), "직관팟 작성자가 아니면 수정할 수 없습니다!");

        partyThumbnailUrlRepository.deleteByPartyId(partyId);
        partyAgeRepository.deleteByPartyId(partyId);
        partyJoinRepository.deleteByPartyId(partyId);
        partyRepository.deleteById(partyId);

        Long chatRoomId = partyDocument.getChatRoomId();

        chatMessageRepository.deleteAllByChatRoomId(chatRoomId);
        chatParticipantRepository.deleteByChatRoomId(chatRoomId);
        chatRoomRepository.deleteById(chatRoomId);

        return PartyDeleteResponse.of(partyId);
    }

    public PartyEndResponse terminateParty(CustomOAuth2User principal, Long partyId) {

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("직관팟이 존재하지 않습니다!"));

        if (party.isEndedParty()) {
            throw new PartyException.AlreadyTerminatedException("이미 종료된 직관팟입니다!");
        }

        PartyAssert.isLoginUserWriter(principal.getId(), party.getWriterId(), "작성자가 아니면 직관팟을 종료할 수 없습니다!");

        party.terminateParty();

        return PartyEndResponse.of(party.getId());
    }

    public void applyPartyFCFS(CustomOAuth2User oauth2User, Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("직관팟이 존재하지 않습니다!"));

        Long userId = validateApplyCondition(oauth2User, partyId, party);

        partyJoinRepository.save(PartyJoin.create(userId, party, PartyJoinRequestStatus.ACCEPT, null));
        party.increaseCurrentParticipants();

        notificationFeignClient.notifyParty(PartyNotificationEvent.joined(
                party.getId(), party.getTitle(), party.getWriterId(), userId
        ));

        chatParticipantRepository.save(ChatParticipant.of(party.getChatRoom(), userId));
    }

    public PartyApplyResponse applyParty(CustomOAuth2User oauth2User, Long partyId, String requireMessage) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("직관팟이 존재하지 않습니다!"));

        Long userId = validateApplyCondition(oauth2User, partyId, party);

        partyJoinRepository.save(PartyJoin.create(userId, party, PartyJoinRequestStatus.WAIT, requireMessage));

        notificationFeignClient.notifyParty(PartyNotificationEvent.request(
                partyId, party.getTitle(), party.getWriterId(), userId, PartyJoinRequestStatus.WAIT.getMessage(), requireMessage
        ));

        return PartyApplyResponse.of("직관팟 신청에 성공했습니다!");
    }

//    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(PresignedUrlForSaveImageRequest request) {
//        return new PresignedUrlForSaveImageResponse(s3Service.generatePresignedUrl(request.imageFileName()));
//    }

    // 나이, 성별 검증은 이전 승인제 직관팟 신청에서 검증함!
    public PartyApproveResponse approveParty(Long loginUserId, Long partyId, PartyApproveRequest request) {

        // 1. 직관팟 불러오기
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("직관팟이 존재하지 않습니다!"));

        if (party.getPartyJoinMethod() == PartyJoinMethod.FIRST_COME) {
            throw new PartyException.InvalidApproveRequestToPartyException("선착순 모집인 직관팟에는 승인 요청을 보낼 수 없습니다!");
        }

        Long writerId = party.getWriterId();
        Long applicantUserId = request.applicantUserId();

        // 2. 로그인한 유저가 직관팟 작성자가 맞는지 확인 (작성자 아니면 승인 불가!)
        PartyAssert.isLoginUserWriter(loginUserId, writerId, "작성자가 아니면 승인할 수 없습니다!");

        // 3. partyId와 applicantUserId 가진 PartyJoin 가져오기
        PartyJoin partyJoin = partyJoinRepository.findByPartyIdAndUserId(partyId, applicantUserId)
                .orElseThrow(() -> new NotFoundException("직관팟 지원자가 아닙니다!"));

        // 4. isApproved 가 false 일 경우 PartyJoin refused 로 바꾸고 response return
        //                  true 일 경우 PartyJoin approved 로 바꿈
        if (request.isApproved()) {
            party.increaseCurrentParticipants();
            partyJoin.approve();
            partyJoinRepository.save(partyJoin);
            notificationFeignClient.notifyParty(PartyNotificationEvent.approveResult(
                    partyId, party.getTitle(), writerId, loginUserId, true)
            );
        } else {
            partyJoin.refuse();
            partyJoinRepository.save(partyJoin);
            notificationFeignClient.notifyParty(PartyNotificationEvent.approveResult(
                    partyId, party.getTitle(), writerId, loginUserId, false)
            );
            return PartyApproveResponse.of("직관팟 가입 신청 거절 성공했습니다!");
        }

        // 5. partyId와 user 가진 ChatPart 저장
        chatParticipantRepository.save(ChatParticipant.of(party.getChatRoom(), applicantUserId));

        // 6. response return
        return PartyApproveResponse.of("직관팟 가입 신청 승인 성공했습니다!");
    }

    public PartyLeaveResponse leaveParty(CustomOAuth2User principal, Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("직관팟이 존재하지 않습니다!"));

        Long loginUserId = principal.getId();
        PartyAssert.isParticipatedPartyAsWriter(loginUserId, party.getWriterId(), "방장은 직관팟을 삭제해 주세요!");

        PartyJoin partyJoin = partyJoinRepository.findByPartyIdAndUserId(party.getId(), loginUserId)
                .orElseThrow(() -> new ApplicantNotFoundException("직관팟에 참여한 사람만 탈퇴할 수 있습니다!"));

        PartyAssert.isAcceptedUser(partyJoin.getPartyJoinRequestStatus());

        chatRoomService.exitChatRoom(party.getChatRoom().getId(), loginUserId);

        partyJoinRepository.delete(partyJoin);
        party.decreaseCurrentMember();

        return PartyLeaveResponse.of("직관팟 탈퇴에 성공하셨습니다!");
    }

    public PartyCancelResponse cancelParty(CustomOAuth2User principal, Long partyId) {

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("직관팟이 존재하지 않습니다!"));

        if (PartyJoinMethod.FIRST_COME.equals(party.getPartyJoinMethod())) {
            throw new NotAllowedToFirstComePartyException("선착순 직관팟에는 지원하지 않는 기능입니다!");
        }

        Long loginUserId = principal.getId();
        PartyAssert.isParticipatedPartyAsWriter(loginUserId, party.getWriterId(), "방장은 직관팟을 삭제해 주세요!");

        PartyJoin partyJoin = partyJoinRepository.findByPartyIdAndUserId(party.getId(), loginUserId)
                .orElseThrow(() -> new ApplicantNotFoundException("직관팟에 신청한 사람만 탈퇴할 수 있습니다!"));

        PartyAssert.isWaitingUser(partyJoin.getPartyJoinRequestStatus());

        partyJoinRepository.delete(partyJoin);

        return PartyCancelResponse.of("직관팟 신청 취소에 성공하셨습니다!");
    }

    private Long validateApplyCondition(CustomOAuth2User oauth2User, Long partyId, Party party) {
        Long userId = oauth2User.getId();
        Gender userGender = oauth2User.getUserDto().getGender();
        PartyAgeGroup userAgeGroup = PartyAgeGroup.getAgeGroupByAge(oauth2User.getUserDto().getAge());

        PartyAssert.isParticipatedPartyAsWriter(userId, party.getWriterId(), "직관팟 작성자는 지원할 수 없습니다!");

        throwIfAlreadyAppliedToParty(userId, partyId);

        List<PartyAgeGroup> partyAgeGroupList = partyAgeReadOnlyRepository.findByPartyId(party.getId())
                .stream()
                .map(partyAgeDocument -> PartyAgeGroup.getAgeGroupByAge(partyAgeDocument.getAge()))
                .toList();

        PartyAssert.isAppliableParty(party, partyAgeGroupList, userGender, userAgeGroup);
        return userId;
    }

    private void updatePartyAgeGroup(PartyUpdateRequest updateRequest, Long partyId, Party savedParty) {
        partyAgeRepository.deleteByPartyId(partyId);
        partyAgeRepository.saveAll(
                updateRequest.ageGroup().stream()
                        .map(age -> PartyAge.create(savedParty, PartyAgeGroup.getAgeByDescription(age)))
                        .toList()
        );
    }

    private void updatePartyThumbnails(PartyUpdateRequest updateRequest, Long partyId, Party savedParty) {
        partyThumbnailUrlRepository.deleteByPartyId(partyId);
        partyThumbnailUrlRepository.saveAll(
                updateRequest.thumbnailImageNameList().stream()
                        .map(thumbnailUrl -> PartyThumbnailUrl.create(savedParty, thumbnailUrl))
                        .toList()
        );
    }

    private ChatRoom initializeChatRoomAsWriter(Long userId) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create());
        chatParticipantRepository.save(ChatParticipant.of(chatRoom, userId));
        return chatRoom;
    }

    private void saveThumbnailUrlIfPresent(PartyCreateRequest request, Party party) {
        if (thumbnailUrlExistsIn(request)) {
            List<PartyThumbnailUrl> partyThumbnailUrlList = toPartyThumbnailUrlEntity(request, party);
            partyThumbnailUrlRepository.saveAll(partyThumbnailUrlList);
        }
    }

    private static List<PartyThumbnailUrl> toPartyThumbnailUrlEntity(PartyCreateRequest request, Party party) {
        return request.thumbnailImageNameList().stream()
                .map(thumbnailUrl -> PartyThumbnailUrl.create(party, thumbnailUrl))
                .toList();
    }

    private static boolean thumbnailUrlExistsIn(PartyCreateRequest request) {
        return !Objects.isNull(request.thumbnailImageNameList()) && !request.thumbnailImageNameList().isEmpty();
    }

    private static List<PartyAge> toPartyAgeEntity(PartyCreateRequest request, Party party) {
        return request.ageGroup().stream()
                .map(age -> PartyAge.create(party, PartyAgeGroup.getAgeByDescription(age)))
                .toList();
    }

    private void throwIfAlreadyAppliedToParty(Long userId, Long partyId) {
        partyJoinReadOnlyRepository.findByUserIdAndPartyId(userId, partyId)
                .ifPresent(partyJoinDocument ->
                        PartyJoinRequestStatus.throwIfAlreadyAppliedToParty(partyJoinDocument.getPartyJoinRequestStatus())
                );
    }
}
