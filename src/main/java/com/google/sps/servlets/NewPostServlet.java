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
        // for clarity I've decided to only deal with the images part in this branch. 

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
            .set("photoURL", imageURL)
            .build();
        datastore.put(postEntity);

    }

    /*
    1. uploads an (img) file to google cloud storage
    2. returns the uploaded img file's url (as String) 
    */
    private static String cloudStorageUpload(String fileName, InputStream fileInputStream) {
        //should edit once project id actually decided
        String projectId = "summer22-sps-24.uc.r";
        String bucketName = "summer22-sps-24.uc.r.appspot.com";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Upload file to cloud storage
        Blob blob = storage.create(blobInfo, fileInputStream);
        // Return the uploaded file's url
        return blob.getMediaLink();
    }
}