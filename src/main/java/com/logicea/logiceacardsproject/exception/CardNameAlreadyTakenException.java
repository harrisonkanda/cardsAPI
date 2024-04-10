package com.logicea.logiceacardsproject.exception;

public class CardNameAlreadyTakenException extends RuntimeException{
    public CardNameAlreadyTakenException(String msg) {
        super(msg);
    }
}
