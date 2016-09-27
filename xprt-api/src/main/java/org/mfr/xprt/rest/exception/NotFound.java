package org.mfr.xprt.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="object not found")
public class NotFound extends RuntimeException{
	public NotFound(){
		
	}
	public NotFound(String message){
		super(message);
	}
}
