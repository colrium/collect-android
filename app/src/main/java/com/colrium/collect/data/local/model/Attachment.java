package com.colrium.collect.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@Entity(tableName = Attachment.TABLE_NAME)
public class Attachment {
    public static final String TABLE_NAME = "attachments";

    @SerializedName("id")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id = new ObjectId().toString();

    @SerializedName("uuid")
    @ColumnInfo(name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @SerializedName("expires")
    @ColumnInfo(name = "expires")
    private String expires;

    @SerializedName("accessibility")
    @ColumnInfo(name = "accessibility")
    private String accessibility;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("date_attached")
    @ColumnInfo(name = "date_attached")
    private String dateAttached;

    @SerializedName("attached_by")
    @ColumnInfo(name = "attached_by")
    private String attachedBy;

    @SerializedName("path")
    @ColumnInfo(name = "path")
    private String path;

    @SerializedName("created_on")
    @ColumnInfo(name = "created_on")
    private String createdOn;

    @SerializedName("location")
    @ColumnInfo(name = "location")
    private String location;

    @SerializedName("last_updated_on")
    @ColumnInfo(name = "last_updated_on")
    private String lastUpdatedOn;



    @SerializedName("md5")
    @ColumnInfo(name = "md5")
    private String md5;

    public void setExpires(String expires){
        this.expires = expires;
    }

    public String getExpires(){
        return expires;
    }

    public void setAccessibility(String accessibility){
        this.accessibility = accessibility;
    }

    public String getAccessibility(){
        return accessibility;
    }




    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public String getUuid(){
        return uuid;
    }

    public void setDateAttached(String dateAttached){
        this.dateAttached = dateAttached;
    }

    public String getDateAttached(){
        return dateAttached;
    }

    public void setAttachedBy(String attachedBy){
        this.attachedBy = attachedBy;
    }

    public String getAttachedBy(){
        return attachedBy;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public void setCreatedOn(String createdOn){
        this.createdOn = createdOn;
    }

    public String getCreatedOn(){
        return createdOn;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
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


    public void setMd5(String md5){
        this.md5 = md5;
    }

    public String getMd5(){
        return md5;
    }
}
