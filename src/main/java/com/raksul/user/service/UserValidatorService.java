package com.raksul.user.service;


import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorService {
	
	
	public boolean validatEmailId(String email) {
		return EmailValidator.getInstance().isValid(email);
	}
	public boolean validatePassword(String password) {
		if(password.length() < 8)
			return false;
		else 
			return true;
	}
}
