// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/** Fetches all sumbitted posts from the server and list them to the DOM. **/
function loadPosts() {
    // "/list-posts" is a servlet fetch all posts from the server;
    // Class Name for each post is Post
    // Each new Post named as post;
    // The List<Post> is named as posts;
    fetch('/load-post').then(response => response.json()).then((posts) => {
    const postListElement = document.getElementById('post-container');
    posts.forEach((post) => {
        console.log(post);
        postListElement.appendChild(createPostElement(post));
      })
    });
}

/** Creates an element that represents a post, including its delete button. */
function createPostElement(post) {
    const postElement = document.createElement('li');
    postElement.className = 'post';
    
    const titleElement = document.createElement('span');
    titleElement.innerText = post.textValue;
    
    // create a button to delete an elemnet
    const deleteButtonElement = document.createElement('button');
    deleteButtonElement.innerText = 'Delete';
    deleteButtonElement.addEventListener('click', () => {
        delete post;
    // Remove an element from the DOM.
    postElement.remove();
    });
    
    postElement.appendChild(titleElement);
    postElement.appendChild(deleteButtonElement);
    return postElement;
}

// for reference: 
// new Promise((resolve, reject) => {

//     throw new Error("Whoops!");
  
//   }).catch(function(error) {
  
//     alert("The error is handled, continue normally");
  
//   }).then(() => alert("Next successful handler runs"));

// const validateLocation = new Promise((resolve, reject) => {
//     throw new Error ("error!");
// }).catch(function() {
//     var geocoder = new google.maps.Geocoder();
//     var location = document.getElementById("location");
//     new google.maps.places.Autocomplete(location);
//     geocoder.geocode({
//         'address': location.value
//     }, function(results, status) {
//         if (status === google.maps.GeocoderStatus.OK && results.length > 0) {
//             location.value = results[0].formatted_address;
//         } else {
//            alert("Invalid address");
//         }
//     });
// }.then(() => {
//     console.log("success?");
// })

function validateLocation() {
    // you can refer to geocoder here: https://developers.google.com/maps/documentation/javascript/geocoding
    var location = document.getElementById("location");
    new google.maps.places.Autocomplete(location);
    if (location.value) {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({
            'address': location.value
        }, function(results, status) {
            if (status === google.maps.GeocoderStatus.OK && results.length > 0) {
                location.value = results[0].formatted_address;
            } else {
               alert("Invalid address");
            }
        });
    }
}

const firebaseApp = firebase.initializeApp({
    apiKey: "AIzaSyAv-Zy1ZpAwC31yVhtcSgGIj3OmTUbkE5Y",
    authDomain: "login-with-firebase-data-b3557.firebaseapp.com",
    databaseURL: "https://login-with-firebase-data-b3557-default-rtdb.firebaseio.com",
    projectId: "login-with-firebase-data-b3557",
    storageBucket: "login-with-firebase-data-b3557.appspot.com",
    messagingSenderId: "155976060237",
    appId: "1:155976060237:web:7f8d2e6eab1f1895f93b04"
}); //should change to team's firebase! currently juhee's own initializations.

const db = firebaseApp.firestore();
const auth = firebaseApp.auth();

const signout = () => {
    auth.signOut().then(() => {
      alert("Signed Out Successfully!")
    }).catch((error) => {
      console.log(error.code)
      console.log(error.message)
      alert(error.message)
    })
}

auth.onAuthStateChanged(function(user) {
    if (!user) {
        // User is signed out
        // Redirect to Log In
        window.location.replace("https://jhong-sps-summer22.appspot.com");
    }
});

// const register = () => {
//     const fullname = document.getElementById('fullname').value;
//     const email = document.getElementById('email').value;
//     const password = document.getElementById('password').value;
//     console.log(email, password);
//     auth.createUserWithEmailAndPassword(email, password)
//     .then((res) => {
//       console.log(res.user)
//       db.collection('users')
//         .add({
//             fullname: fullname,
//             email: email,
//             password: password
//         })
//         .then((docRef) => {
//             console.log("Document written with ID: ", docRef.id);
//         })
//         .catch((error) => {
//             console.error("Error adding document: ", error);
//       })
//       alert("Registered Successfully!");
//     })
//     .catch((error) => {
//       console.log(error.code)
//       console.log(error.message)
//       alert(error.message)
//     })
// }

// const login = () => {
//     const email = document.getElementById('email').value;
//     const password = document.getElementById('password').value;
  
//     auth.signInWithEmailAndPassword(email, password)
//     .then((res) => {
//       console.log(res.user)
//       alert("Logged In Successfully!")
//     })
//     .catch((error) => {
//       console.log(error.code)
//       console.log(error.message)
//       alert(error.message)
//     })
// }

// const signout = () => {
//     auth.signOut().then(() => {
//       alert("Signed Out Successfully!")
//     }).catch((error) => {
//       console.log(error.code)
//       console.log(error.message)
//       alert(error.message)
//     })
// }

// auth.onAuthStateChanged(function(user) {
//     if (user) {
//       //user exists and is logged in
//       //redirect Home
//       window.location.replace("https://jhong-sps-summer22.appspot.com/main.html");
//     }
// });