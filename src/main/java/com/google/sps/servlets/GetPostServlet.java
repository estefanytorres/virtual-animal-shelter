package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import com.google.sps.data.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for loading posts. */
@WebServlet("/posts")
public class GetPostServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Post").setOrderBy(OrderBy.desc("timePosted")).build();
    QueryResults<Entity> results = datastore.run(query);

    List<Post> posts = new ArrayList<>();
    while (results.hasNext()) {
      Entity entity = results.next();

      long id = entity.getKey().getId();
      String petName = entity.getString("pet name");
      String location = entity.getString("location");
      String animalType = entity.getString("animal type");
      String breed = entity.getString("breed");
      String birthday = entity.getString("birthday");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate dob = LocalDate.parse(birthday, formatter);
      String gen = entity.getString("gender");
      Gender gender = Gender.valueOf(gen);
      String vaccination = entity.getString("vaccination");
      String sickness = entity.getString("sickness");
      String email = entity.getString("email");
      String phone = entity.getString("phone");
      String stat = entity.getString("status");
      Status status = Status.valueOf(stat);
      long tp = entity.getLong("time-posted");
      Post post = new Post(id, petName, location, animalType, breed, dob, gen, vac, sick, email, phone, tp);
      posts.add(post);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(posts));
  }
}