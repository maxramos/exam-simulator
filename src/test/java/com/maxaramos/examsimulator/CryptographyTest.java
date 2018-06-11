package com.maxaramos.examsimulator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class CryptographyTest {

	@Test
	public void testCryptography() {
		try {
			Date now = new Date();
			System.out.println("now:" + now);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", 1);
			jsonObj.put("expiry", now.getTime());
			String token = jsonObj.toString();
			System.out.println("Token: " + token);

			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(128);
			SecretKey secretKey = keygen.generateKey();
			byte[] encodedSecretKey = secretKey.getEncoded();
			System.out.println("Secret Key: " + Base64.getEncoder().encodeToString(encodedSecretKey));

			Cipher encryptor = Cipher.getInstance("AES");
			encryptor.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedToken = encryptor.doFinal(token.getBytes());
			String encodedEncryptedToken = Base64.getEncoder().encodeToString(encryptedToken);
			System.out.println("Encrypted: " + encodedEncryptedToken);

			Cipher decryptor = Cipher.getInstance("AES");
			decryptor.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decodedEncryptedToken = Base64.getDecoder().decode(encodedEncryptedToken);
			byte[] decryptedToken = decryptor.doFinal(decodedEncryptedToken);
			String _token = new String(decryptedToken);
			System.out.println("Decrypted: " + _token);

			JSONObject _jsonObj = new JSONObject(_token);
			int id = _jsonObj.getInt("id");
			Date expiry = new Date(_jsonObj.getLong("expiry"));
			System.out.println("id:" + id + ", expiry:" + expiry);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
