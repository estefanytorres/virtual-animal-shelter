package com.google.sps.data;
import java.time.*;
/*
if you want to use the Gender and Status enums in other files, do these:
import com.google.sps.data.Post;
import com.google.sps.data.Post.Gender;
import com.google.sps.data.Post.Status;
*/

public class Post {

    public enum Gender {
        MALE,
        FEMALE,
        UNSPECIFIED
    }
    
    public enum Status {
        OPEN,
        CLOSED
    } // open to other suggestions for the Status options! 

    private long id;
    private String petName;
    private String location;
    private String animalType;
    private String breed;
    private LocalDate dob; 
    private ZonedDateTime timePosted; //We want to unify the times to be in UTC. We can use ZonedDateTime to set zone id to UTC, which is handled in the ListPostServlet.java
    private Gender gender;
    private String vaccination;
    private String sickness;
    private String email;
    private String phone;
    private Status status;
    private String photoURL;

    public Post(long id, String petName, String location, String animalType, String breed, 
                LocalDate dob, Gender gen, String vac, String sick, String email, String phone, ZonedDateTime tp, String photoURL) {
        this.id = id;
        this.petName = petName;
        this.location = location;
        this.animalType = animalType;
        this.breed = breed;
        this.dob = dob;
        this.gender = gen;  
        this.vaccination = vac;
        this.sickness = sick;
        this.email = email;
        this.phone = phone;
        this.status = Status.OPEN;
        this.timePosted = tp;
        this.photoURL = photoURL;
    }

    public void setStatus(Status s) {
        this.status = s;
    }

}