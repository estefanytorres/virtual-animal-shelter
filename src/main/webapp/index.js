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
    if (user) {
      //user exists and is logged in
      //redirect Home
      window.location.replace("https://jhong-sps-summer22.appspot.com");
      // when redirected to homepage show username
    } 
});

// need to implement sign out√ (and if time permits, also delete account functions)
// also need to make sure we load to the correct html page once successfully signed in