package com.google.sps.servlets;


import com.google.sps.data.Post;
import com.google.sps.data.Post.Gender;
import com.google.sps.data.Post.Status;
import java.time.format.DateTimeFormatter;
import java.time.*;

import com.google.appengine.api.datastore.Query;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import com.google.sps.data.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/load-by-id")
public class ListPostByIDServlet extends HttpServlet {
    KeyFactory keyFactory;
    Datastore datastore;

    public ListPostByIDServlet(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("Post");
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter("id");
        Query<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("Post")
            .setFilter(PropertyFilter.eq("breed", "c1"))
            //.setFilter(PropertyFilter.gt("__key__", "5712837116690432")) tpye wrong
            //.setFilter(PropertyFilter.gt("__key__", keyFactory.newKey("5691975521009664")))
            //.setFilter(PropertyFilter.eq("id", "5691975521009664"))
            //.setFilter(PropertyFilter.eq("id", stringId))
            //.setFilter(PropertyFilter.hasAncestor(keyFactory.newKey(stringId)))
            //.setFilter(PropertyFilter.eq("__key__", keyFactory.newKey(stringId)))
            .build();
        QueryResults<Entity> results = DatastoreOptions.getDefaultInstance().getService().run(query);
    
        List<Post> posts = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (results.hasNext()) {
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
            long tp = entity.getLong("timePosted");
            ZonedDateTime z = ZonedDateTime.ofInstant(Instant.ofEpochMilli(tp), ZoneId.systemDefault());
            ZonedDateTime timePosted = z.withZoneSameInstant(ZoneId.of("UTC"));
            String photoURL = entity.getString("photoURL");
    
            Post post = new Post(id, petName, location, animalType, breed, dob, gender, vaccination, sickness, email, phone, timePosted, photoURL);
            posts.add(post);

        }

        Gson gson = new Gson();

        response.setContentType("application/json;");
        response.getWriter().println(keyFactory.newKey(stringId));
        response.getWriter().println(keyFactory.newKey("5691975521009664"));
        //response.getWriter().println(stringId);
        //response.getWriter().println(String.valueOf(results.hasNext()));
        response.getWriter().println(gson.toJson(posts));
      }
}
