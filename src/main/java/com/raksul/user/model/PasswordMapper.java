package com.raksul.user.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PasswordMapper implements RowMapper<String> {

	public String mapRow(ResultSet resultSet, int i) throws SQLException {
		
		return resultSet.getString("password");

	}
}
