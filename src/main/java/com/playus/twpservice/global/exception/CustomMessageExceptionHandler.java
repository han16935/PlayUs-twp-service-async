package com.playus.twpservice.global.exception;

import com.playus.twpservice.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
//import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
//import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

//@Slf4j
//@ControllerAdvice
//public class CustomMessageExceptionHandler {
//
//    private static final String LOG_FORMAT = "Class: {}, Code : {}, Message : {}";
//
//    @MessageExceptionHandler(IllegalArgumentException.class)
//    @SendToUser(destinations = "/queue/errors", broadcast = false)
//    public ErrorResponse handleBadRequest(IllegalArgumentException e) {
//        String errorMessage = e.getMessage();
//        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST, errorMessage);
//        return ErrorResponse.badRequestError(errorMessage);
//    }
//
//    @MessageExceptionHandler(RuntimeException.class)
//    @SendToUser(destinations = "/queue/errors", broadcast = false)
//    public ErrorResponse handleRuntimeException(RuntimeException e) {
//        String errorMessage = e.getMessage();
//        log.error(LOG_FORMAT, e.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
//        return ErrorResponse.internalServerError(errorMessage);
//    }
//}
