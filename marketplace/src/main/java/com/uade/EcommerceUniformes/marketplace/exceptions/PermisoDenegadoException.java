package com.uade.EcommerceUniformes.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class PermisoDenegadoException extends RuntimeException {
    public PermisoDenegadoException(String message) {
        super(message);
    }
}
