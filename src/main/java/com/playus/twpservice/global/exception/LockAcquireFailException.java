package com.playus.twpservice.global.exception;

public class LockAcquireFailException extends RuntimeException{
    public LockAcquireFailException(String message) {
        super(message);
    }
}
