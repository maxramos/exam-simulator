package com.maxaramos.examsimulator.controller.ws;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxaramos.examsimulator.service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/auth")
	public Map<String, Object> authenticate(
			@RequestHeader(name = "X-Auth-Username", required = true) String username,
			@RequestHeader(name = "X-Auth-Password", required = true) String password) {
		String authToken = authService.authenticate(username, password);
		Map<String, Object> result = new HashMap<>();
		result.put("token", authToken);
		return result;
	}

}
