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

  function validateLocation() {
      // you can refer to geocoder here: https://developers.google.com/maps/documentation/javascript/geocoding
      var geocoder = new google.maps.Geocoder();
      var location = document.getElementById("location");
      new google.maps.places.Autocomplete(location);
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
  //google.maps.event.addDomListener(window, 'load', validateLocation);
  //import {auth} from './index.js'; --> why doesn't this work?
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
  const register = () => {
      // const username = document.getElementById('username').value;
      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;
      console.log(email, password);
      auth.createUserWithEmailAndPassword(email, password)
      .then((res) => {
        console.log(res.user)
        db.collection('users')
          .add({
              // username: username,
              email: email,
              password: password
          })
          .then((docRef) => {
              console.log("Document written with ID: ", docRef.id);
          })
          .catch((error) => {
              console.error("Error adding document: ", error);
        })
        alert("Registered Successfully!");
      })
      .catch((error) => {
        console.log(error.code)
        console.log(error.message)
        alert(error.message)
      })
  }
  const login = () => {
      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;
    
      auth.signInWithEmailAndPassword(email, password)
      .then((res) => {
        console.log(res.user)
        alert("Logged In Successfully!")
      })
      .catch((error) => {
        console.log(error.code)
        console.log(error.message)
        alert(error.message)
      })
  }
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
      // if (!user) {
      //     // User is signed out
      //     // Redirect to Log In
      //     //window.location.replace("https://jhong-sps-summer22.appspot.com");
      //     //alert (not actual alert user though) that the user has been signed out, stop showing their username
      // }
      if (user) {
            // if (window.location == "https://jhong-sps-summer22.appspot.com/sign_in_sign_up.html") {
            //     window.location = "https://jhong-sps-summer22.appspot.com";
            // }
            if (window.location != "https://summer22-sps-24.appspot.com") {
                window.location = "https://summer22-sps-24.appspot.com/";
            }
            const displayEmail = user.email;
            var welcomeDiv = document.getElementById("welcome");
            welcomeDiv.innerHTML = "You're logged in with " + displayEmail + ". Welcome!";
            var newPost_btn = document.getElementById("newPost_btn");
            newPost_btn.style.display = 'block';
      } else {
            var welcomeDiv = document.getElementById("welcome");
            welcomeDiv.innerHTML = "You're not logged in! Log in to create a new post.";
            var newPost_btn = document.getElementById("newPost_btn");
            newPost_btn.style.display = 'none';
      }
  }); 

/*


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








