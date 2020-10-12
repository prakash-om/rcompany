package com.raksul.user.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raksul.user.dao.UserDaoImpl;
import com.raksul.user.model.ErrorResponse;
import com.raksul.user.model.TokenResponse;
import com.raksul.user.model.User;
import com.raksul.user.model.UserResponse;
import com.raksul.user.service.TokenService;
import com.raksul.user.service.UserValidatorService;
import com.raksul.user.utils.Constants;

@RestController
public class UserController {

	@Autowired
	UserValidatorService userValidatorService;

	@Autowired
	UserDaoImpl userDaoImpl;

	@Autowired
	TokenService tokeService;

	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity registerUser(@RequestBody User user) {

		if (null == user) {
			return errorResponse("Provider valid input", HttpStatus.BAD_REQUEST);
		}

		if (null == user.getEmail() || null == user.getPassword()) {
			return errorResponse("Provider valid input", HttpStatus.BAD_REQUEST);
		}

		if (!userValidatorService.validatEmailId(user.getEmail())) {
			return errorResponse("validation error: email", HttpStatus.BAD_REQUEST);
		}

		if (!userValidatorService.validatePassword(user.getPassword())) {
			return errorResponse("validation error: password", HttpStatus.BAD_REQUEST);
		}

		UserResponse result;
		try {
			result = userDaoImpl.insertUser(user);
		} catch(DuplicateKeyException ex) {
			return errorResponse("Duplicate email are not allowed", HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return errorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<UserResponse>(result, HttpStatus.OK);

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity login(@RequestBody User user) throws NoSuchAlgorithmException {

		if (null == user) {
			return errorResponse("Provider valid input", HttpStatus.BAD_REQUEST);
		}

		if (null == user.getEmail() || null == user.getPassword()) {
			return errorResponse("Provider valid input", HttpStatus.BAD_REQUEST);
		}

		if (!userValidatorService.validatEmailId(user.getEmail())) {
			return errorResponse("validation error: email", HttpStatus.BAD_REQUEST);
		}

		if (!userValidatorService.validatePassword(user.getPassword())) {
			return errorResponse("validation error: password", HttpStatus.BAD_REQUEST);
		}

		if (userDaoImpl.validUser(user)) {

			String token = tokeService.generateToken();
			userDaoImpl.insertToken(user.getEmail(), token);
			HashMap<String, String> map = new HashMap<>();
			map.put("token", token);
			return new ResponseEntity<HashMap>(map, HttpStatus.OK);

		} else {
			return errorResponse("Invalid User", HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(value = "/secret", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getSecret(@RequestHeader(value = "Authorization") String authToken) {

		if (null == authToken || authToken.isEmpty()) {
			return errorResponse("Token invalid", HttpStatus.UNAUTHORIZED);
		}

		String token = authToken.replace(Constants.PREFIX, "");
		UserResponse responseUser;

		try {
			responseUser = userDaoImpl.selectUserByToken(token);
		} catch (Exception e) {
			return errorResponse("Token invalid", HttpStatus.UNAUTHORIZED);
		}

		TokenResponse tokenResponse;
		if ((null != responseUser) && (!responseUser.getId().isEmpty())) {
			tokenResponse = new TokenResponse();
			tokenResponse.setUser_id(Long.parseLong(responseUser.getId()));
			return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

		}
		return errorResponse("Token invalid", HttpStatus.UNAUTHORIZED);

	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity patch(@RequestBody User user, @PathVariable("id") String id)
			throws NoSuchAlgorithmException {

		if (null == id) {
			return errorResponse("provide user id", HttpStatus.BAD_REQUEST);
		}

		if (null == user) {
			return errorResponse("Provider valid input", HttpStatus.BAD_REQUEST);
		}

		if (null != user.getEmail() && !userValidatorService.validatEmailId(user.getEmail())) {
			return errorResponse("validation error: email", HttpStatus.BAD_REQUEST);
		}

		if (null != user.getPassword() && !userValidatorService.validatePassword(user.getPassword())) {
			return errorResponse("validation error: password", HttpStatus.BAD_REQUEST);
		}

		try {
			UserResponse response = userDaoImpl.updateUser(user, id);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return errorResponse("Invalid User", HttpStatus.UNAUTHORIZED);
		}

	}

	private ResponseEntity errorResponse(String errorMsg, HttpStatus status) {
		ErrorResponse responseError = new ErrorResponse();
		responseError.setError(errorMsg);
		return new ResponseEntity<>(responseError, status);
	}

}
