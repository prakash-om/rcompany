package com.raksul.user.dao;

import com.raksul.user.model.UserResponse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import com.raksul.user.model.User;

public interface UserDao {
	UserResponse insertUser(User user) throws NoSuchAlgorithmException;
	
	boolean validUser(User user) throws NoSuchAlgorithmException;
	
	void insertToken(String email, String token);
	
	UserResponse selectUserByToken(String token);
	
	UserResponse updateUser(User user, String id) throws NoSuchAlgorithmException;
	
}
