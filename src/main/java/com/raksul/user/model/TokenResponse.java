package com.raksul.user.model;

public class TokenResponse {

	long user_id;
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public final String secret = "All your base are belong to us";
}
