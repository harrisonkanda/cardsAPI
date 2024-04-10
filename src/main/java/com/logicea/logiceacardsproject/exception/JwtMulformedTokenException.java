package com.logicea.logiceacardsproject.exception;

import javax.naming.AuthenticationException;

public class JwtMulformedTokenException extends RuntimeException {

    public JwtMulformedTokenException(String msg) {
        super(msg);
    }
}
