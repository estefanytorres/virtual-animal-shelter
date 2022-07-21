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

/*
/** Fetches all sumbitted posts from the server and list them to the DOM. 
function loadPosts() {
    // "/load-post" is a servlet fetch all posts from the server;
    // Class Name for each post is Post
    // Each new Post named as post;
    // The List<Post> is named as posts;
    fetch("/load-post").then(response => response.json()).then((posts) => {
        const postListElement = document.getElementById('post-container');
        posts.forEach((post) => {
            postListElement.appendChild(createPostElement(post));

      })
    });
}
**/

/** Creates an element that represents a post, including its delete button. 
function createPostElement(post) {
    const postElement = document.createElement('li');
    postElement.className = 'post';
    
    const nameElement = document.createElement('strong');
    nameElement.innerText = "Pet Name: " + post.petName;

    const typeElement = document.createElement('div');
    typeElement.innerText = "Animal Type: " + post.animalType;

    const locElement = document.createElement('div');
    locElement.innerText = "Location: " + post.location;

    const ageElement = document.createElement('div');
    var dob = new Date(post.dob.year, post.dob.month, post.dob.day);
    ageElement.innerText = "Age: "+ getAge(dob);

    postElement.appendChild(nameElement);
    postElement.appendChild(typeElement);
    postElement.appendChild(locElement);
    postElement.appendChild(ageElement);

    return postElement;
}
*/


/** Caculate the age based on birthday. */
function getAge(birthDate) 
{
    let result;
    var today = new Date(); //returns date object of current date and time
    var y_diff = today.getFullYear() - birthDate.getFullYear();
    var m_diff = today.getMonth() - birthDate.getMonth();

    if (m_diff < 0) 
    {
        y_diff--;
        m_diff = 12 + m_diff;
    }

    if (y_diff < 0) {
        result = "Unborn Baby"
    } else {
        result = y_diff.toString() + " years " + m_diff.toString() + " months old";
    }

    return result;
}

/** Fetches all sumbitted posts from the server with search function based animalType and location. **/
function loadPosts() {
    const userCardTemplate = document.querySelector("[data-user-template]");
    const userCardContainer = document.querySelector("[data-user-cards-container]");
    const searchInput = document.querySelector("[data-search]");

    let lists = [];

    searchInput.addEventListener("input", (e)=> {
        const value = e.target.value.toLowerCase();
        lists.forEach(post => {
            const isVisible = post.animalType.toLowerCase().includes(value) || post.location.toLowerCase().includes(value)
            post.element.classList.toggle("hide", !isVisible)
        })
    })
    fetch("/load-post").then(response => response.json()).then(posts => {
        lists = posts.map(post => {
            const card = userCardTemplate.content.cloneNode(true).children[0];
            const header = card.querySelector("[data-header]")
            const location = card.querySelector("[data-location]")
            const type = card.querySelector("[data-type]")
            const age = card.querySelector("[data-age]")
            const image = card.querySelector("[data-image]")
            header.textContent = post.petName;
            location.textContent = post.location;
            type.textContent = post.animalType;
            var dob = new Date(post.dob.year, post.dob.month, post.dob.day);
            age.textContent = getAge(dob);
            image.setAttribute('src', post.photoURL);
            userCardContainer.append(card);
            return {name: post.petName, location: post.location, animalType: post.animalType, age:age.textContent, element:card}
      })
    });
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