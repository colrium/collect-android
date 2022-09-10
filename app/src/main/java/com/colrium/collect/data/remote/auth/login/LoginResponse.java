package com.colrium.collect.data.remote.auth.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private ResponseData data;

	public String getMessage() {
		return message;
	}

	public LoginResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	public ResponseData getData() {
		return data;
	}

	public LoginResponse setData(ResponseData data) {
		this.data = data;
		return this;
	}
}