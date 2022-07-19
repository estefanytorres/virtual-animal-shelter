package com.google.sps.servlets;

import com.google.sps.data.Post;
import com.google.sps.data.Post.Gender;
import com.google.sps.data.Post.Status;
import java.time.format.DateTimeFormatter;
import java.time.*;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import com.google.sps.data.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/load-post")
public class ListPostServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query =
            Query.newEntityQueryBuilder().setKind("Post").setOrderBy(OrderBy.desc("timePosted")).build();
        QueryResults<Entity> results = datastore.run(query);
    
        List<Post> posts = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (results.hasNext()) {
            Entity entity = results.next();
    
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
            //String stat = entity.getString("status");
            //Status status = Status.valueOf(stat);
            long tp = entity.getLong("timePosted");
            ZonedDateTime z = ZonedDateTime.ofInstant(Instant.ofEpochMilli(tp), ZoneId.systemDefault());
            ZonedDateTime timePosted = z.withZoneSameInstant(ZoneId.of("UTC"));
            String photoURL = entity.getString("photoURL");
    
            Post post = new Post(id, petName, location, animalType, breed, dob, gender, vaccination, sickness, email, phone, timePosted, photoURL);
            posts.add(post);

            // response.getWriter().println(photoURL);
            // response.getWriter().println(timePosted);
            // response.getWriter().println(phone);
            // response.getWriter().println(petName);
            // output the photoURL as an <img> element in HMTL
            // response.setContentType("text/html;");
            // String imgTag = String.format("<img src=\"%s\" />", photoURL);
            // response.getWriter().println(imgTag);
            // response.getWriter().println("<br>");
            //also need to check for a case where there's no image uploaded (don't we want to make images mandatory though?)

        }

        Gson gson = new Gson();

        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(posts));
      }
}

