package com.maxaramos.examsimulator.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid username or password.")
public class AuthException extends RuntimeException {

	private static final long serialVersionUID = 8763203683839365222L;

}
