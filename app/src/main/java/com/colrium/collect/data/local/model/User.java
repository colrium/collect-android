package com.colrium.collect.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.json.JSONArray;

@Entity(tableName = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "users";


    @SerializedName("_id")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id = new ObjectId().toString();

    @SerializedName("uuid")
    @ColumnInfo(name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @SerializedName("country")
    @ColumnInfo(name = "country")
    private String country;

    @SerializedName("education")
    @ColumnInfo(name = "education")
    private String education;

    @SerializedName("role")
    @ColumnInfo(name = "role")
    private String role;

    @SerializedName("gender")
    @ColumnInfo(name = "gender")
    private String gender;

    @SerializedName("documents")
    @Ignore
    private ArrayList<Attachment> documents;

    @SerializedName("documents_ids")
    @ColumnInfo(name = "documents_ids")
    private ArrayList<String> documentsIds;

    @SerializedName("city")
    @ColumnInfo(name = "city")
    private String city;

    @SerializedName("icon")
    @ColumnInfo(name = "icon")
    private String icon;

    @SerializedName("rating")
    @ColumnInfo(name = "rating")
    private Long rating;

    @SerializedName("last_known_coordinates")
    @Embedded
    private ArrayList<Double> lastKnownCoordinates;

    @SerializedName("experience")
    @ColumnInfo(name = "experience")
    private String experience;



    @SerializedName("institution")
    @ColumnInfo(name = "institution")
    private String institution;

    @SerializedName("interest")
    @ColumnInfo(name = "interest")
    private String interest;

    @SerializedName("staff_id")
    @ColumnInfo(name = "staff_id")
    private String staffId;

    @SerializedName("course")
    @ColumnInfo(name = "course")
    private String course;

    @SerializedName("tokens")
    @ColumnInfo(name = "tokens")
    private ArrayList<String> tokens;

    @SerializedName("company")
    @ColumnInfo(name = "company")
    private String company;

    @SerializedName("presence")
    @ColumnInfo(name = "presence")
    private String presence;

    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    private String firstName;

    @SerializedName("job_title")
    @ColumnInfo(name = "job_title")
    private String jobTitle;

    @SerializedName("honorific")
    @ColumnInfo(name = "honorific")
    private String honorific;

    @SerializedName("address")
    @ColumnInfo(name = "address")
    private String address;

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    private String lastName;

    @SerializedName("avatar")
    @Ignore
    private Attachment avatar;

    @SerializedName("avatar_id")
    @ColumnInfo(name = "avatar_id")
    private String avatarId;

    @SerializedName("middle_name")
    @ColumnInfo(name = "middle_name")
    private String middleName;

    @SerializedName("user_sector")
    @ColumnInfo(name = "user_sector")
    private String userSector;

    @SerializedName("tags")
    @ColumnInfo(name = "tags")
    private ArrayList<String> tags;

    @SerializedName("email_address")
    @ColumnInfo(name = "email_address")
    private String emailAddress;

    @SerializedName("last_known_position")
    @Ignore
    private Object lastKnownPosition;

    @SerializedName("phone_number")
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;



    @SerializedName("region")
    @ColumnInfo(name = "region")
    private String region;

    @SerializedName("contacts")
    @ColumnInfo(name = "contacts")
    private String contacts;

    @SerializedName("user_groups")
    @ColumnInfo(name = "user_groups")
    private ArrayList<String> userGroups;

    @SerializedName("status")
    @ColumnInfo(name = "status")
    private String status;

    public User () {
        this.id = new ObjectId().toString();
        this.uuid = UUID.randomUUID().toString();
    }
    @Ignore
    public User(String id) {
        this.id = id;
        this.uuid = UUID.randomUUID().toString();
    }

    public void setCountry(String country){
        this.country = country;

    }

    public String getCountry(){
        return country;
    }

    public void setEducation(String education){
        this.education = education;

    }

    public String getEducation(){
        return education;

    }

    public void setRole(String role){
        this.role = role;

    }

    public String getRole(){
        return role;
    }

    public void setGender(String gender){
        this.gender = gender;

    }

    public String getGender(){
        return gender;
    }

    public void setDocuments(ArrayList<Attachment> documents){
        this.documents = documents;

    }

    public ArrayList<Attachment> getDocuments(){
        return documents;
    }

    public void setCity(String city){
        this.city = city;

    }

    public String getCity(){
        return city;
    }

    public void setIcon(String icon){
        this.icon = icon;

    }

    public Attachment getAvatar() {
        return avatar;
    }

    public void setAvatar(Attachment avatar) {
        this.avatar = avatar;

    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;

    }

    public void setStatus(String status) {
        this.status = status;

    }

    public String getIcon(){
        return icon;
    }

    public void setRating(Long rating){
        this.rating = rating;

    }

    public Long getRating(){
        return rating;
    }

    public void setLastKnownCoordinates(ArrayList<Double> lastKnownCoordinates){
        this.lastKnownCoordinates = lastKnownCoordinates;
    }

    public ArrayList<Double> getLastKnownCoordinates(){
        return lastKnownCoordinates;
    }

    public void setExperience(String experience){
        this.experience = experience;

    }

    public String getExperience(){
        return experience;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;

    }

    public String getUuid(){
        return uuid;
    }

    public void setInstitution(String institution){
        this.institution = institution;

    }

    public String getInstitution(){
        return institution;
    }

    public void setInterest(String interest){
        this.interest = interest;

    }

    public String getInterest(){
        return interest;
    }

    public void setStaffId(String staffId){
        this.staffId = staffId;

    }

    public String getStaffId(){
        return staffId;
    }

    public void setCourse(String course){
        this.course = course;

    }

    public String getCourse(){
        return course;
    }

    public void setTokens(ArrayList<String> tokens){
        this.tokens = tokens;

    }

    public ArrayList<String> getTokens(){
        return tokens;
    }

    public void setCompany(String company){
        this.company = company;

    }

    public String getCompany(){
        return company;
    }

    public void setPresence(String presence){
        this.presence = presence;

    }

    public String getPresence(){
        return presence;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;

    }

    public String getFirstName(){
        return firstName;
    }

    public void setJobTitle(String jobTitle){
        this.jobTitle = jobTitle;

    }

    public String getJobTitle(){
        return jobTitle;
    }

    public void setHonorific(String honorific){
        this.honorific = honorific;

    }

    public String getHonorific(){
        return honorific;
    }

    public void setAddress(String address){
        this.address = address;

    }

    public String getAddress(){
        return address;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;

    }

    public String getLastName(){
        return lastName;
    }

    public void setAttachment(Attachment avatar){
        this.avatar = avatar;

    }

    public Attachment getAttachment(){
        return avatar;
    }

    public void setMiddleName(String middleName){
        this.middleName = middleName;

    }

    public String getMiddleName(){
        return middleName;
    }

    public void setUserSector(String userSector){
        this.userSector = userSector;

    }

    public String getUserSector(){
        return userSector;
    }

    public void setTags(ArrayList<String> tags){
        this.tags = tags;

    }

    public ArrayList<String> getTags(){
        return tags;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;

    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setLastKnownPosition(Object lastKnownPosition){
        this.lastKnownPosition = lastKnownPosition;

    }

    public Object getLastKnownPosition(){
        return lastKnownPosition;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;

    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setId(String id){
        this.id = id;

    }

    public String getId(){
        return id;
    }

    public void setRegion(String region){
        this.region = region;

    }

    public String getRegion(){
        return region;
    }

    public void setContacts(String contacts){
        this.contacts = contacts;

    }

    public String getContacts(){
        return contacts;
    }

    public void setUserGroups(ArrayList<String> userGroups){
        this.userGroups = userGroups;

    }

    public ArrayList<String> getUserGroups(){
        return userGroups;
    }


    public String getStatus(){
        return status;
    }

    public ArrayList<String> getDocumentsIds() {
        return documentsIds;
    }

    public void setDocumentsIds(ArrayList<String> documentsIds) {
        this.documentsIds = documentsIds;
    }

    public String getDisplayName() {
        String displayName = "";
        if (firstName != null){
            displayName += firstName;
        }
        if (lastName != null){
            if (!displayName.isEmpty() && !lastName.isEmpty())
                displayName += " ";
            displayName += lastName;
        }
        return displayName;
    }
    public String name() {
        String name = "";
        if (firstName != null){
            name += firstName;
        }
        if (lastName != null){
            if (!name.isEmpty() && !lastName.isEmpty())
                name += " ";
            name += lastName;
        }
        return name;
    }
}
