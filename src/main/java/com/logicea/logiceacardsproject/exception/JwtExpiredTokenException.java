package com.logicea.logiceacardsproject.exception;

import javax.naming.AuthenticationException;

public class JwtExpiredTokenException extends RuntimeException {

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }
}
