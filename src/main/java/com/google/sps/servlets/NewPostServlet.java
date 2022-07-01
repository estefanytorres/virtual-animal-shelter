package com.google.sps.servlets;

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

// import java.io.IOException;
// import java.io.InputStream;
// import java.io.ByteArrayInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/new-post") // open to changes in what it's called! 
@MultipartConfig
public class NewPostServlet extends HttpServlet {
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // would sanitisation be necessary here?
        // the names inside the getParameter/getPart should match with html element attributes (should talk with Rui) 

        // Get all details about the animal inputted by the user
        String petName = request.getParameter("pet_name");
        String location = request.getParameter("location");
        String animalType = request.getParameter("animal_type");
        String breed = request.getParameter("breed");
        String dob = request.getParameter("birthday"); 
        String gender = request.getParameter("gender");//Gender.valueOf(request.getParameter("gender"));
        String vaccination = request.getParameter("vacinnation");
        String sickness = request.getParameter("sickness");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String status = request.getParameter("status"); //Status.valueOf(request.getParameter("status"));
        long timePosted = System.currentTimeMillis();

        // Get the img file chosen by the user
        Part image = request.getPart("image");
        String imgFileName = image.getSubmittedFileName();
        InputStream imgInputStream = image.getInputStream();

        // Upload the file to cloud storage
        String imageURL = cloudStorageUpload(imgFileName, imgInputStream);

        // Store other animal data to datastore
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

    /*
    1. uploads an (img) file to google cloud storage
    2. returns the uploaded img file's url (as String) 
    */
    private static String cloudStorageUpload(String fileName, InputStream fileInputStream) {
        //should edit once project id actually decided
        String projectId = "YOUR_PROJECT_ID";
        String bucketName = "YOUR_PROJECT_ID.appspot.com";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
        byte[] buff = new byte[8000];
        int bytesRead = 0;

        while((bytesRead = fileInputStream.read(buff)) != -1) {
            byteArr.write(buff, 0, bytesRead);
        }

        byte[] data = byteArr.toByteArray();
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(data);

        // Upload the file to Cloud Storage.
        Blob blob = storage.create(blobInfo, baInputStream);
        /* both create with input stream and byte array input stream seem deprecated. 
           I tried using Files.readallbytes (see https://cloud.google.com/storage/docs/uploading-objects#prereq-code-samples)
           but couldn't quite figure out how to get the filepaths of the images users uplaoded */

        // Return the uploaded file's URL.
        return blob.getMediaLink();
    }
}

/*
For reference:

FormHandlerServlet.java
@WebServlet("/upload")
@MultipartConfig
public class FormHandlerServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Get the message entered by the user.
    String message = request.getParameter("message");

    // Get the file chosen by the user.
    Part filePart = request.getPart("image");
    String fileName = filePart.getSubmittedFileName();
    InputStream fileInputStream = filePart.getInputStream();

    // Upload the file and get its URL
    String uploadedFileUrl = uploadToCloudStorage(fileName, fileInputStream);

    // Output some HTML that shows the data the user entered.
    // You could also store the uploadedFileUrl in Datastore instead.
    PrintWriter out = response.getWriter();
    out.println("<p>Here's the image you uploaded:</p>");
    out.println("<a href=\"" + uploadedFileUrl + "\">");
    out.println("<img src=\"" + uploadedFileUrl + "\" />");
    out.println("</a>");
    out.println("<p>Here's the text you entered:</p>");
    out.println(message);
  }

  //Uploads a file to Cloud Storage and returns the uploaded file's URL. 
  private static String uploadToCloudStorage(String fileName, InputStream fileInputStream) {
    String projectId = "YOUR_PROJECT_ID";
    String bucketName = "YOUR_PROJECT_ID.appspot.com";
    Storage storage =
        StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    BlobId blobId = BlobId.of(bucketName, fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

    // Upload the file to Cloud Storage.
    Blob blob = storage.create(blobInfo, fileInputStream);

    // Return the uploaded file's URL.
    return blob.getMediaLink();
  }
}

*/