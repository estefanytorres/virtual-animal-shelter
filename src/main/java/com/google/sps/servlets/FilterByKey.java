package com.google.sps.servlets;

import com.google.sps.data.Post;
import com.google.sps.data.Post.Gender;
import java.time.format.DateTimeFormatter;
import java.time.*;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/load-by-id")
public class FilterByKey extends HttpServlet {
    KeyFactory keyFactory;
    Datastore datastore;

    public FilterByKey(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("Post");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String stringId = request.getParameter("id");        
        Key key = keyFactory.newKey(Long.parseLong(stringId));
        Entity result = datastore.get(key);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Entity entity = result;
    
        long id = entity.getKey().getId();
        String petName = entity.getString("petName");
        String location = entity.getString("location");
        String animalType = entity.getString("animalType");
        String breed = entity.getString("breed");
        String birthday = entity.getString("birthday");
        LocalDate dob = LocalDate.parse(birthday, formatter);
        String gen = entity.getString("gender");
        Gender gender = Gender.valueOf(gen);
        String vaccination = entity.getString("vaccination");
        String sickness = entity.getString("sickness");
        String email = entity.getString("email");
        String phone = entity.getString("phone");
        long tp = entity.getLong("timePosted");
        ZonedDateTime z = ZonedDateTime.ofInstant(Instant.ofEpochMilli(tp), ZoneId.systemDefault());
        ZonedDateTime timePosted = z.withZoneSameInstant(ZoneId.of("UTC"));
        String photoURL = entity.getString("photoURL");
    
        Post post = new Post(id, petName, location, animalType, breed, dob, gender, vaccination, sickness, email, phone, timePosted, photoURL);
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(post));
      }
}