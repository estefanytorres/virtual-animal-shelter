package com.google.sps.servlets;

import java.util.*;
import com.google.sps.data.Post;
import com.google.sps.data.Post.Gender;
import com.google.sps.data.Post.Status;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.jsoup.Jsoup;
import org.apache.commons.lang3.EnumUtils;

@WebServlet("/new-post") // open to changes in what it's called! 
@MultipartConfig
public class NewPostServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Get the img file chosen by the user
        Part image = request.getPart("image");
        String imgFileName = UUID.randomUUID().toString(); //image.getSubmittedFileName(); 
        InputStream imgInputStream = image.getInputStream();

        // // Upload the file to cloud storage
        String imageURL = cloudStorageUpload(imgFileName, imgInputStream);

        // Get all options from Status and Gender enums as sets
        Set<String> genderSet = EnumUtils.getEnumMap(Gender.class).keySet(); // set of options in Gender enum

        // Get parameters from request (part 1)
        String petName = request.getParameter("petName");
        String location = request.getParameter("location");
        String animalType = request.getParameter("animalType");
        String breed = request.getParameter("breed");
      
        // Convert birthday/dob param (String) to the proper LocalDate object for Post constructor; also validate the dob
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dob = request.getParameter("birthday"); 
        LocalDate.parse(dob, formatter.withResolverStyle(ResolverStyle.STRICT)); //checks both formatting and validness of dates
        LocalDate.parse(dob, formatter).isAfter(LocalDate.now().minusYears(80)); 
        // checks that the birthday is within the last 80 years
        // considering many different species for pets, 80 years seem more than enough. (parrots live up to ~70 years old apparently)

        // Validate gender
        String gender = request.getParameter("gender");
        if (!genderSet.contains(gender)) {
            //if the gender from request is not a valid gender, we error. 
            //What kind of an error should we throw? Or what action should we take?
        }

        // Get parameters from request (part 2)
        String vaccination = request.getParameter("vacinnation");
        String sickness = request.getParameter("sickness");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Get the time posted (considered "now")
        long timePosted = System.currentTimeMillis();
      
        // Store other animal data to datastore
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("Post");
        FullEntity postEntity = Entity.newBuilder(keyFactory.newKey())
                .set("petName", petName)
                .set("location", location)
                .set("animalType", animalType)
                .set("breed", breed)
                .set("birthday", dob)
                .set("gender", gender) // would this work with radios?
                .set("vaccination", vaccination)
                .set("sickness", sickness)
                .set("email", email)
                .set("phone", phone)
                .set("timePosted", timePosted)
                //.set("status", status)
                .set("photoURL", imageURL)
                .build();
        datastore.put(postEntity);
        response.sendRedirect("/index.html");
        
    }

    /*
    1. uploads an (img) file to google cloud storage
    2. returns the uploaded img file's url (as String) 
    */
    private static String cloudStorageUpload(String fileName, InputStream fileInputStream) {
        //should edit once project id actually decided
        String projectId = "summer22-sps-24";
        String bucketName = "summer22-sps-24.appspot.com";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Upload file to cloud storage
        Blob blob = storage.create(blobInfo, fileInputStream);
        // Return the uploaded file's url
        return blob.getMediaLink();
    }
}

