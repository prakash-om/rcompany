package com.raksul.user.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<UserResponse> {

	public UserResponse mapRow(ResultSet resultSet, int i) throws SQLException {

		UserResponse user = new UserResponse();
		user.setId(resultSet.getString("id"));
		user.setEmail(resultSet.getString("email"));
		user.setCreatedAt(resultSet.getString("createdAt"));
		user.setUpdatedAt(resultSet.getString("updatedAt"));
		
		return user;
	}
}
