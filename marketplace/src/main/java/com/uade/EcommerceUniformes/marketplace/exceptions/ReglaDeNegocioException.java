package com.uade.EcommerceUniformes.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ReglaDeNegocioException extends RuntimeException {
    public ReglaDeNegocioException(String message) {
        super(message);
    }
}
