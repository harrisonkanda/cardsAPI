package com.logicea.logiceacardsproject.exception;

public class JwtExpiredTokenException extends RuntimeException {

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }
}
