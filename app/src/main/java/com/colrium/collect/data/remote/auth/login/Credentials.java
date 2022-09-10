package com.colrium.collect.data.remote.auth.login;

import com.google.gson.annotations.SerializedName;

import com.colrium.collect.config.Constants;

public class Credentials{

	@SerializedName("password")
	private String password;

	@SerializedName("grant_type")
	private String grantType = Constants.DEFAULT_GRANT_TYPE;

	@SerializedName("client_secret")
	private String clientSecret = Constants.CLIENT_SECRET;

	@SerializedName("client_id")
	private String clientId = Constants.CLIENT_ID;

	@SerializedName("username")
	private String username;

	public Credentials setPassword(String password){
		this.password = password;
		return this;
	}

	public String getPassword(){
		return password;
	}

	public Credentials setGrantType(String grantType){
		this.grantType = grantType;
		return this;
	}

	public String getGrantType(){
		return grantType;
	}

	public Credentials setClientSecret(String clientSecret){
		this.clientSecret = clientSecret;
		return this;
	}

	public String getClientSecret(){
		return clientSecret;
	}

	public Credentials setClientId(String clientId){
		this.clientId = clientId;
		return this;
	}

	public String getClientId(){
		return clientId;
	}

	public Credentials setUsername(String username){
		this.username = username;
		return this;
	}

	public String getUsername(){
		return username;
	}
}