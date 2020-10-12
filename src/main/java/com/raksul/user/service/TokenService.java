package com.raksul.user.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raksul.user.dao.UserDaoImpl;

@Service
public class TokenService {
	
	@Autowired
	UserDaoImpl userDaoImpl;

	public String generateToken() throws NoSuchAlgorithmException {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}

		byte[] messageDigest = md.digest(UUID.randomUUID().toString().getBytes());
		String result = bytesToHex(messageDigest);
		
		return result.toUpperCase();
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
