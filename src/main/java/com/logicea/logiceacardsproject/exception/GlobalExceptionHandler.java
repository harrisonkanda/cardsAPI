package com.logicea.logiceacardsproject.exception;

import com.logicea.logiceacardsproject.dto.response.ErrorsResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorsResponseDto> handleDataNotFoundException(DataNotFoundException ex) {

        return new ResponseEntity<>(ErrorsResponseDto.builder()
                .message(ex.getMessage())
                .cause(ex.getCause().getLocalizedMessage())
                .status(HttpStatus.NOT_FOUND.value()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorsResponseDto> handleUserNotFoundException(UserNotFoundException ex) {

        return new ResponseEntity<>(ErrorsResponseDto.builder()
                .message(ex.getMessage())
                .cause(ex.getCause().getLocalizedMessage())
                .status(HttpStatus.NOT_FOUND.value()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtExpiredTokenException.class)
    public ResponseEntity<ErrorsResponseDto> handleJWTExpiredTokenException(JwtExpiredTokenException ex) {

        return new ResponseEntity<>(ErrorsResponseDto.builder()
                .message(ex.getMessage())
                .cause(ex.getCause().getLocalizedMessage())
                .status(HttpStatus.UNAUTHORIZED.value()).build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorsResponseDto> handleBadCredentialsException(BadCredentialsException ex) {

        return new ResponseEntity<>(ErrorsResponseDto.builder()
                .message(ex.getMessage())
                .cause(ex.getCause().getLocalizedMessage())
                .status(HttpStatus.UNAUTHORIZED.value()).build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtMulformedTokenException.class)
    public ResponseEntity<ErrorsResponseDto> handleMissingOrMulformedTokenException(JwtMulformedTokenException ex) {

        return new ResponseEntity<>(ErrorsResponseDto.builder()
                .message(ex.getMessage())
                .cause(ex.getCause().getLocalizedMessage())
                .status(HttpStatus.UNAUTHORIZED.value()).build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CardNameAlreadyTakenException.class)
    public ResponseEntity<ErrorsResponseDto> handleCardNameAlreadyTakenException(CardNameAlreadyTakenException ex) {

        return new ResponseEntity<>(ErrorsResponseDto.builder()
                .message(ex.getMessage())
                .cause(ex.getCause().getLocalizedMessage())
                .status(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorsResponseDto> handleAccessDeniedException(AccessDeniedException ex) {

        return new ResponseEntity<>(ErrorsResponseDto.builder()
                .message(ex.getMessage())
                .cause(ex.getCause().getLocalizedMessage())
                .status(HttpStatus.UNAUTHORIZED.value()).build(), HttpStatus.UNAUTHORIZED);
    }
}
