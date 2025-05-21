package com.SuaraCloud.SongService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotDataOwnerException extends RuntimeException {

    public NotDataOwnerException(String message) {
        super(message);
    }
}