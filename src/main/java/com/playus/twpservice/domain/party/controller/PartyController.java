package com.playus.twpservice.domain.party.controller;

import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import com.playus.twpservice.domain.party.dto.appliedparty.AppliedPartyResponse;
import com.playus.twpservice.domain.party.dto.applieduser.PartyAppliedUserResponse;
import com.playus.twpservice.domain.party.dto.apply.PartyApplyResponse;
import com.playus.twpservice.domain.party.dto.apply.PartyApproveApplyRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveRequest;
import com.playus.twpservice.domain.party.dto.approve.PartyApproveResponse;
import com.playus.twpservice.domain.party.dto.cancel.PartyCancelResponse;
import com.playus.twpservice.domain.party.dto.delete.PartyDeleteResponse;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailRequest;
import com.playus.twpservice.domain.party.dto.detail.PartyDetailResponse;
import com.playus.twpservice.domain.party.dto.end.PartyEndResponse;
import com.playus.twpservice.domain.party.dto.info.PartyInfoRequest;
import com.playus.twpservice.domain.party.dto.info.PartyInfoResponse;
import com.playus.twpservice.domain.party.dto.create.PartyCreateRequest;
import com.playus.twpservice.domain.party.dto.create.PartyCreateResponse;
import com.playus.twpservice.domain.common.request.PartyIdRequest;
import com.playus.twpservice.domain.party.dto.leave.PartyLeaveResponse;
import com.playus.twpservice.domain.party.dto.participants.PartyParticipantsInfoResponse;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateRequest;
import com.playus.twpservice.domain.party.dto.update.PartyUpdateResponse;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.twpservice.domain.party.dto.presigned.PresignedUrlForSaveImageResponse;
import com.playus.twpservice.domain.party.facade.PartyApplyFacade;
import com.playus.twpservice.domain.party.service.PartyReadOnlyService;
import com.playus.twpservice.domain.party.service.PartyService;
import com.playus.twpservice.domain.party.specification.PartyControllerSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/party")
@RequiredArgsConstructor
public class PartyController implements PartyControllerSpecification {

    private final PartyService partyService;
    private final PartyReadOnlyService partyReadOnlyService;
    private final PartyApplyFacade partyApplyFacade;

    @PostMapping
    public ResponseEntity<PartyCreateResponse> createParty(@AuthenticationPrincipal CustomOAuth2User principal, @Valid @RequestBody PartyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createParty(principal.getId(), request));
    }

    @GetMapping
    public List<PartyInfoResponse> getPartiesByMatchId(@Valid PartyInfoRequest request) {
        return partyReadOnlyService.getPartyInfoListByMatchId(request.matchId());
    }

    @GetMapping("/{partyId}")
    public PartyDetailResponse getPartyDetail(@Valid PartyDetailRequest request) {
        return partyReadOnlyService.getPartyDetail(request.partyId());
    }

    @PutMapping("/{partyId}")
    public PartyUpdateResponse updateParty(@AuthenticationPrincipal CustomOAuth2User principal, @Valid PartyIdRequest idRequest,
                                           @Valid @RequestBody PartyUpdateRequest request) {
        return partyService.updateParty(principal.getId(), idRequest, request);
    }

    @PatchMapping("/{partyId}")
    public PartyDeleteResponse deleteParty(@AuthenticationPrincipal CustomOAuth2User principal, @Valid PartyIdRequest idRequest) {
        return partyService.deleteParty(principal.getId(), idRequest.partyId());
    }

    @PatchMapping("/{partyId}/end")
    public PartyEndResponse terminateParty(@AuthenticationPrincipal CustomOAuth2User principal, @Valid PartyIdRequest idRequest) {
        return partyService.terminateParty(principal, idRequest.partyId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{partyId}/apply/fcfs")
    public PartyApplyResponse applyPartyFCFS(@AuthenticationPrincipal CustomOAuth2User principal, @Valid PartyIdRequest idRequest) {
        partyApplyFacade.applyParty(principal, idRequest.partyId());
        return PartyApplyResponse.of("직관팟 가입에 성공했습니다!");
    }

    @PostMapping("/{partyId}/apply")
    public PartyApplyResponse applyParty(@AuthenticationPrincipal CustomOAuth2User principal, @Valid PartyIdRequest idRequest,
                                         @Valid @RequestBody PartyApproveApplyRequest request) {
        return partyService.applyParty(principal, idRequest.partyId(), request.requireMessage());
    }

    @PatchMapping("/{partyId}/approve")
    public PartyApproveResponse approveParty(@AuthenticationPrincipal CustomOAuth2User principal,
                                             @Valid PartyIdRequest idRequest,
                                             @Valid @RequestBody PartyApproveRequest request) {
        return partyService.approveParty(principal.getId(), idRequest.partyId(), request);
    }

    @GetMapping("/{partyId}/approved-applicants")
    public List<PartyAppliedUserResponse> getAppliedUsers(
            @AuthenticationPrincipal CustomOAuth2User principal,
            @PathVariable Long partyId) {
        return partyReadOnlyService.getAppliedUsers(principal.getId(), partyId);
    }

    @GetMapping("/{partyId}/participants")
    public List<PartyParticipantsInfoResponse> getParticipants(
            @AuthenticationPrincipal CustomOAuth2User principal,
            @Valid PartyIdRequest idRequest
    ) {
        return partyReadOnlyService.getParticipants(principal, idRequest.partyId());
    }


    @PostMapping("/{partyId}/leave")
    public PartyLeaveResponse leaveParty(@AuthenticationPrincipal CustomOAuth2User principal,
                                         @Valid PartyIdRequest idRequest) {
        return partyService.leaveParty(principal, idRequest.partyId());
    }

    @PatchMapping("/{partyId}/cancel")
    public PartyCancelResponse cancelParty(@AuthenticationPrincipal CustomOAuth2User principal,
                                           @Valid PartyIdRequest idRequest) {
        return partyService.cancelParty(principal, idRequest.partyId());
    }

    @GetMapping("/applied-parties")
    public List<AppliedPartyResponse> getAppliedParties(@AuthenticationPrincipal CustomOAuth2User principal) {
        return partyReadOnlyService.getAppliedParties(principal);
    }

//    @PostMapping("/presigned-url")
//    public PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(@Valid @RequestBody PresignedUrlForSaveImageRequest request) {
//        return partyService.generatePresignedUrlForSaveImage(request);
//    }
}
