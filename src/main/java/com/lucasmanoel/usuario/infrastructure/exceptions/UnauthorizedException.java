package com.lucasmanoel.usuario.infrastructure.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
