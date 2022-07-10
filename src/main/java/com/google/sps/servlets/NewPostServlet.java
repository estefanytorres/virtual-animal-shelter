package com.google.sps.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Set;
import java.util.function.DoubleConsumer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.sps.data.Post.Gender;
import com.google.sps.data.Post.Status;

import org.apache.commons.lang3.EnumUtils;

@WebServlet("/new-post") // open to changes in what it's called! 
public class NewPostServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // would sanitisation be necessary here?
        // the names inside the getParameter should match with html element attributes 
        Set<String> genderSet = EnumUtils.getEnumMap(Gender.class).keySet(); // set of options in Gender enum

        String petName = request.getParameter("petName");
        String location = request.getParameter("location");
        String animalType = request.getParameter("animalType");
        String breed = request.getParameter("breed");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dob = request.getParameter("dob"); 
        // This line cause dob format error
        // LocalDate.parse(dob, formatter.withResolverStyle(ResolverStyle.STRICT)); //checks both formatting and validness of dates
        LocalDate.parse(dob, formatter).isAfter(LocalDate.now().minusYears(80)); 
        // checks that the birthday is within the last 80 years
        // considering many different species for pets, 80 years seem more than enough. (parrots live up to ~70 years old apparently)

        String gender = request.getParameter("gender");
        if (!genderSet.contains(gender)) {
            //if the gender from request is not a valid gender, we error. 
            //What kind of an error should we throw? Or what action should we take?
        }

        String vaccination = request.getParameter("vacinnation");
        String sickness = request.getParameter("sickness");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        long timePosted = System.currentTimeMillis();

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("Post");
        FullEntity postEntity =
            Entity.newBuilder(keyFactory.newKey())
                .set("petName", petName)
                .set("location", location)
                .set("animalType", animalType)
                .set("breed", breed)
                .set("dob", dob)
                .set("gender", gender) // would this work with radios?
                .set("vaccination", vaccination)
                .set("sickness", sickness)
                .set("email", email)
                .set("phone", phone)
                .set("timePosted", timePosted)
                .build();
        datastore.put(postEntity);

    // Write the value to the response so the user can see it.
    response.getWriter().println("Your Post has been submitted!");

    }
}