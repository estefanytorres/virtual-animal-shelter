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
        FEMALE
    }
    
    public enum Status {
        OPEN,
        CLOSED
    }

    public enum Name {
        firstname,
        lastname
    }

    private long id;
    private String petName;
    private String location;
    private String animalType;
    private String breed;
    private LocalDate dob; 
    private ZonedDateTime timePosted;
    private Gender gender;
    private String vaccination;
    private String sickness;
    private String email;
    private String phone;
    private Status status;

    public Post(long id, String petName, String location, String animalType, String breed, 
                LocalDate dob, Gender gen, String vac, String sick, String email, String phone, ZonedDateTime tp) {
        this.id = id;
        this.petName = petName;
        this.location = location;
        this.animalType = animalType;
        this.breed = breed;
        this.dob = dob;
        this.gender = gen;  // not too sure how this is going to work. Should I pull enum outside of this class?
        this.vaccination = vac;
        this.sickness = sick;
        this.email = email;
        this.phone = phone;
        this.status = Status.OPEN;
        this.timePosted = tp;
    }

    public void changeStatus(Status s) {
        this.status = s;
    }

}