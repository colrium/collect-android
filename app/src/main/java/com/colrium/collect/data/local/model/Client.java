package com.colrium.collect.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

@Entity(tableName = Client.TABLE_NAME)
public class Client{
	public static final String TABLE_NAME = "clients";
	@SerializedName("id")
	@PrimaryKey
	@NonNull
	private String id = new ObjectId().toString();

	@SerializedName("uuid")
	private String uuid = UUID.randomUUID().toString();
	@SerializedName("grant_type")
	private ArrayList<String> grantType;

	@SerializedName("created_on")
	private String createdOn;


	@SerializedName("last_updated_on")
	private String lastUpdatedOn;


	@SerializedName("label")
	private String label;

	@SerializedName("client_secret")
	private String clientSecret;


	@SerializedName("client_id")
	private String clientId;

	public Client () {
		this.id = new ObjectId().toString();
		this.uuid = UUID.randomUUID().toString();
	}

	public Client(String id) {
		this.id = id;
		this.uuid = UUID.randomUUID().toString();
	}

	public void setGrantType(ArrayList<String> grantType){
		this.grantType = grantType;
	}

	public ArrayList<String> getGrantType(){
		return grantType;
	}

	public void setCreatedOn(String createdOn){
		this.createdOn = createdOn;
	}

	public String getCreatedOn(){
		return createdOn;
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

	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	public void setClientSecret(String clientSecret){
		this.clientSecret = clientSecret;
	}

	public String getClientSecret(){
		return clientSecret;
	}

	public void setUuid(String uuid){
		this.uuid = uuid;
	}

	public String getUuid(){
		return uuid;
	}

	public void setClientId(String clientId){
		this.clientId = clientId;
	}

	public String getClientId(){
		return clientId;
	}
}