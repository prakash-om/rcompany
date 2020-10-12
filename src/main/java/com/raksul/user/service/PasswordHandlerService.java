package com.raksul.user.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class PasswordHandlerService {
	
	public String encryptPassword(String password) throws NoSuchAlgorithmException {
		String result;
		

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}

		byte[] messageDigest = md.digest(password.getBytes());
		result = bytesToHex(messageDigest);
		
		return result;
	}
	
	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
