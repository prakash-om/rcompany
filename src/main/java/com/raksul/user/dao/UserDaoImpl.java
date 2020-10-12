package com.raksul.user.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.raksul.user.model.PasswordMapper;
import com.raksul.user.model.UserResponse;
import com.raksul.user.service.PasswordHandlerService;

import com.raksul.user.model.User;
import com.raksul.user.model.UserMapper;

@Service
public class UserDaoImpl implements UserDao {

	JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	public PasswordHandlerService passwordHandlerService;

	private String currentTime() {

		String pattern = "yyyy-MM-dd'T'HH:mm:ssXXX";

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String dateTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));

		return dateTime;
	}

	private final String SQL_INSERT_USER = "insert into users(email, password, createdAt, updatedAt, token) values(?,?,?,?,?)";
	private final String SQL_SELECT_USER_BY_EMAIL = "select * from users where email = ?";
	private final String SQL_SELECT_USER_BY_EMAIL_RETURN_PASSWORD = "select (password) from users where email = ?";
	private final String SQL_INSERT_TOKEN = "update users set token = ? , updatedAt = ? where email = ?";
	private final String SQL_SELECT_USER_BY_TOKE = "select * from users where token = ?";
	private final String SQL_SELECT_USER_BY_ID = "select * from users where id = ?";

	private final String SQL_UPDATE_EMAIL = "update users set email = ?, updatedAt = ? where id = ?";
	private final String SQL_UPDATE_PASSWORD = "update users set password = ?, updatedAt = ? where id = ?";
	private final String SQL_UPDATE_EMAIL_UPDATE = "update users set email = ? , password = ? , updatedAt = ? where id = ?";

	@Override
	public UserResponse insertUser(User user) throws NoSuchAlgorithmException {

		String enPassword = passwordHandlerService.encryptPassword(user.getPassword());
		jdbcTemplate.update(SQL_INSERT_USER, user.getEmail(), enPassword, currentTime(), null, null);
		return getInsertedUser(user.getEmail());
	}

	private UserResponse getInsertedUser(String email) {
		return jdbcTemplate.queryForObject(SQL_SELECT_USER_BY_EMAIL, new Object[] { email }, new UserMapper());
	}

	@Override
	public boolean validUser(User user) throws NoSuchAlgorithmException {
		String password;
		try {

			password = jdbcTemplate.queryForObject(SQL_SELECT_USER_BY_EMAIL_RETURN_PASSWORD,
					new Object[] { user.getEmail() }, new PasswordMapper());

		} catch (EmptyResultDataAccessException ex) {
			return false;
		}

		if (null == password || password.isEmpty())
			return false;

		String enPassword = passwordHandlerService.encryptPassword(user.getPassword());
		if (password.equals(enPassword))
			return true;
		else
			return false;
	}

	@Override
	public void insertToken(String email, String token) {
		jdbcTemplate.update(SQL_INSERT_TOKEN, new Object[] { token, currentTime(), email });
	}

	@Override
	public UserResponse selectUserByToken(String token) {
		try {
			return jdbcTemplate.queryForObject(SQL_SELECT_USER_BY_TOKE, new Object[] { token }, new UserMapper());
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public UserResponse updateUser(User user, String id) throws NoSuchAlgorithmException {

		// BigDecimal bigDec = new BigDecimal(id);
		Long bigDec = new Long(id);
		try {
			jdbcTemplate.queryForObject(SQL_SELECT_USER_BY_ID, new Object[] { bigDec }, new UserMapper());
		} catch (Exception e) {
			throw e;
		}

		String enPassword = null;

		if (null != user.getPassword()) {
			enPassword = passwordHandlerService.encryptPassword(user.getPassword());
		}

		if (null != enPassword && null != user.getEmail()) {
			jdbcTemplate.update(SQL_UPDATE_EMAIL_UPDATE,
					new Object[] { user.getEmail(), enPassword, currentTime(), bigDec });
		}

		if (null != enPassword) {
			jdbcTemplate.update(SQL_UPDATE_PASSWORD, new Object[] { enPassword, currentTime(), bigDec });
		}

		if (null != user.getEmail()) {
			jdbcTemplate.update(SQL_UPDATE_EMAIL, new Object[] { user.getEmail(), currentTime(), bigDec });
		}

		return jdbcTemplate.queryForObject(SQL_SELECT_USER_BY_ID, new Object[] { bigDec }, new UserMapper());

	}

}
