package org.mfr.xprt.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="unauthorized client")
public class AuthException extends RuntimeException{

}
