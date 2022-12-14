package com.colrium.collect.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.UUID;



@Entity(tableName = AccessToken.TABLE_NAME, foreignKeys = @ForeignKey(entity=Client.class, parentColumns="id", childColumns="client_id"))
public class AccessToken {
	public static final String TABLE_NAME = "access_tokens";
	@SerializedName("id")
	@PrimaryKey
	@NonNull
	@ColumnInfo(name = "id")
	private String id = new ObjectId().toString();

	@SerializedName("uuid")
	@ColumnInfo(name = "uuid")
	private String uuid = UUID.randomUUID().toString();

	@SerializedName("refresh_token")
	@ColumnInfo(name = "refresh_token")
	private String refreshToken;

	@SerializedName("issue_time")
	@ColumnInfo(name = "issue_time")
	private String issueTime;

	@SerializedName("active")
	@ColumnInfo(name = "active")
	private Boolean active;

	@SerializedName("created_on")
	@ColumnInfo(name = "created_on")
	private String createdOn;

	@SerializedName("scope")
	@ColumnInfo(name = "scope")
	private String scope;

	@SerializedName("client")
	@Ignore
	private Client client;

	@SerializedName("client_id")
	@ColumnInfo(name = "client_id")
	private String clientId;

	@SerializedName("last_updated_on")
	@ColumnInfo(name = "last_updated_on")
	private String lastUpdatedOn;


	@SerializedName("type")
	@ColumnInfo(name = "type")
	private String type;

	@SerializedName("expires_in")
	@ColumnInfo(name = "expires_in")
	private Long expiresIn;


	@SerializedName("token")
	@ColumnInfo(name = "token")
	private String token;

	public void setRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken(){
		return refreshToken;
	}

	public void setCreatedOn(String createdOn){
		this.createdOn = createdOn;
	}

	public String getCreatedOn(){
		return createdOn;
	}

	public void setScope(String scope){
		this.scope = scope;
	}

	public String getScope(){
		return scope;
	}

	public void setClient(Client client){
		this.client = client;
	}

	public Client getClient(){
		return client;
	}

	public void setLastUpdatedOn(String lastUpdatedOn){
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public String getLastUpdatedOn(){
		return lastUpdatedOn;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setExpiresIn(Long expiresIn){
		this.expiresIn = expiresIn;
	}

	public Long getExpiresIn(){
		return expiresIn;
	}

	public void setUuid(String uuid){
		this.uuid = uuid;
	}

	public String getUuid(){
		return uuid;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}