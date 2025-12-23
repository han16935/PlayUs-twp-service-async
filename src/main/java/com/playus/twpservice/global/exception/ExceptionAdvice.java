package com.playus.twpservice.global.exception;

import com.playus.twpservice.domain.chat.exception.common.SubscribeException;
import com.playus.twpservice.domain.chat.exception.common.WebSocketException;
import com.playus.twpservice.domain.chat.exception.entity.ChatMessageException;
import com.playus.twpservice.domain.chat.exception.entity.ChatParticipantException;
import com.playus.twpservice.domain.chat.exception.entity.ChatRoomException;
import com.playus.twpservice.domain.party.exception.document.PartyDocumentException;
import com.playus.twpservice.domain.party.exception.document.PartyJoinDocumentException;
import com.playus.twpservice.domain.party.exception.entity.PartyException;
import com.playus.twpservice.domain.party.exception.enums.PartyAgeGroupException;
import com.playus.twpservice.domain.party.exception.enums.PartyGenderException;
import com.playus.twpservice.domain.party.exception.enums.PartyJoinMethodException;
import com.playus.twpservice.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.core.exception.SdkException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBindException(BindException e) {
        String errorMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ErrorResponse.badRequestError(errorMessage);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({
            PartyGenderException.InvalidDescriptionException.class,
            PartyJoinMethodException.InvalidDescriptionException.class,
            PartyAgeGroupException.InvalidDescriptionException.class,
            PartyException.InvalidApproveRequestToPartyException.class,
            SubscribeException.DuplicateSubscribeException.class,
            SubscribeException.UnSubscriptionException.class,
            ChatMessageException.ChatSendEndPointException.class,
            PartyException.NotAllowedToFirstComePartyException.class
    })
    public ErrorResponse handleBadRequestException(Exception e) {
        String errorMessage = e.getMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ErrorResponse.badRequestError(errorMessage);
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler({
            PartyJoinDocumentException.RefusedApplyUserException.class,
            PartyException.NotPartyWriterException.class,
            PartyException.NotAllowedPartyConditionException.class,
            PartyException.NotAllowedPartyJoinRequestStatusException.class,
    })
    public ErrorResponse handleForbiddenException(Exception e) {
        String errorMessage = e.getMessage();
        return ErrorResponse.forbiddenError(errorMessage);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler({
            WebSocketException.TokenNotExistException.class,
            WebSocketException.TokenExpiredException.class
    })
    public ErrorResponse handleUnauthorizedException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("unauthorizedError Error: {}", errorMessage);
        return ErrorResponse.unauthorizedError(errorMessage);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({
            PartyException.NotFoundException.class,
            PartyException.ParticipantsNotFoundException.class,
            PartyDocumentException.NotFoundException.class,
            ChatRoomException.NotFoundException.class,
            ChatMessageException.NotFoundException.class,
            ChatParticipantException.NotFoundException.class,
            PartyException.ApplicantNotFoundException.class,
    })
    public ErrorResponse handleNotFoundException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Not Found Error: {}", errorMessage);
        return ErrorResponse.notFoundError(errorMessage);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler({
            PartyException.ExceedPartyParticipantsException.class,
            PartyException.InsufficientPartyParticipantsException.class,
            PartyJoinDocumentException.DuplicateApplyException.class,
            PartyException.AlreadyCreatedPartyForPerMatchException.class,
            PartyException.AlreadyTerminatedException.class,
            LockAcquireFailException.class
    })
    public ErrorResponse handleConflictException(Exception e) {
        String errorMessage = e.getMessage();
        log.warn("Conflict Error: {}", errorMessage);
        return ErrorResponse.conflictError(errorMessage);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SdkException.class)
    public ErrorResponse handleSdkException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Object Storage Error: {}", errorMessage);
        return ErrorResponse.internalServerError("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {
            Exception.class
    })
    public ErrorResponse handleOtherException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Unexpected Error: {}", errorMessage);
        return ErrorResponse.internalServerError("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {
            NoFallbackAvailableException.class
    })
    public ErrorResponse handleFailOpenFeignException(NoFallbackAvailableException exception) {
        String errorMessage = exception.getMessage();
        log.error("Unexpected Error: {}", errorMessage);
        return ErrorResponse.internalServerError("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }
}
