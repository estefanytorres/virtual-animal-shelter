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
    fetch('/list-posts').then(response => response.json()).then((posts) => {
    const postListElement = document.getElementById('post-container');
    posts.forEach((post) => {
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




