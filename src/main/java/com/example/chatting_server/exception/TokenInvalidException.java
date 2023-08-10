package com.example.chatting_server.exception;

import com.example.chatting_server.util.ResponseCode;
import lombok.Getter;

@Getter
public class TokenInvalidException extends RuntimeException {
    private ResponseCode responseCode;

    public TokenInvalidException(ResponseCode responseCode) {
        this.responseCode = responseCode;

    }

}
