package com.google.sps.servlets;

import com.google.sps.data.Post;
import com.google.sps.data.Post.Gender;
import com.google.sps.data.Post.Status;
import java.time.*;
import java.time.format.DateTimeFormatter;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;

@WebServlet("/new-post") // open to changes in what it's called! 
public class NewPostServlet extends HttpServlet {
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // would sanitisation be necessary here?
        // the names inside the getParameter should match with html element attributes 

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String petName = request.getParameter("pet_name");
        String location = request.getParameter("location");
        String animalType = request.getParameter("animal_type");
        String breed = request.getParameter("breed");
        String dob = request.getParameter("birthday"); 
        //LocalDate dob = LocalDate.parse(request.getParameter("birthday"), formatter);
        String gender = request.getParameter("gender");//Gender.valueOf(request.getParameter("gender"));
        String vaccination = request.getParameter("vacinnation");
        String sickness = request.getParameter("sickness");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String status = request.getParameter("status"); //Status.valueOf(request.getParameter("status"));
        long timePosted = System.currentTimeMillis();

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("Post");
        FullEntity postEntity =
            Entity.newBuilder(keyFactory.newKey())
                .set("pet name", petName)
                .set("location", location)
                .set("animal type", animalType)
                .set("breed", breed)
                .set("birthday", dob)
                .set("gender", gender) // would this work with radios?
                .set("vaccination", vaccination)
                .set("sickness", sickness)
                .set("email", email)
                .set("phone", phone)
                .set("time-posted", timePosted)
                .set("status", status)
                .build();
        datastore.put(postEntity);

    }
}