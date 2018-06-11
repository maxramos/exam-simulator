package com.maxaramos.examsimulator.service;

import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64.Encoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private Map<Long, JSONObject> tokenMap = new ConcurrentHashMap<>();

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private Encoder base64Encoder;

	public String authenticate(String username, String password) {
		UserDetails user = userDetailsService.loadUserByUsername(username);

		if (user == null || !isPasswordValid(user, password)) {
			throw new AuthException();
		}

		return generateToken(user);
	}

	private boolean isPasswordValid(UserDetails user, String password) {
		if (user.getPassword().equals(password)) {
			return true;
		}

		if (user.getPassword().equals(hash(password))) {
			return true;
		}

		return false;
	}

	private String hash(String password) {
		MessageDigest md = applicationContext.getBean("messageDigest", MessageDigest.class);
		md.update(password.getBytes());
		byte[] digestedPassword = md.digest();
		String hashedPassword = base64Encoder.encodeToString(digestedPassword);
		return hashedPassword;
	}

	private String generateToken(UserDetails user) {
		long tokenId = ThreadLocalRandom.current().nextLong();

		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("username", user.getUsername());
			jsonObj.put("tokenId", tokenId);
			jsonObj.put("expiry", Instant.now().plus(Duration.ofMinutes(30)));
			tokenMap.put(tokenId, jsonObj);

			String token = jsonObj.toString();
			byte[] encryptedToken = applicationContext.getBean("aesEncryptor", Cipher.class).doFinal(token.getBytes());
			String encodedEncryptedToken = base64Encoder.encodeToString(encryptedToken);
			return encodedEncryptedToken;
		} catch (JSONException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException("Error in generating auth token.", e);
		}
	}

}
