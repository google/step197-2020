import { createMap, PinList } from "./home.js";
import React from "react";
import ReactDOM from "react-dom";

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get("user");

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace("/");
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById("page-title").innerText = parameterUsername;
  document.title = parameterUsername + " - User Page";
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showMessageFormIfViewingSelf() {
  fetch("/login-status")
    .then(response => {
      return response.json();
    })
    .then(loginStatus => {
      if (loginStatus.isLoggedIn && loginStatus.username == parameterUsername) {
        const messageForm = document.getElementById("message-form");
        messageForm.classList.remove("hidden");
        document.getElementById("about-me-form").classList.remove("hidden");
      }
    });
}

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const url = "/messages?user=" + parameterUsername;
  fetch(url)
    .then(response => {
      return response.json();
    })
    .then(messages => {
      const messagesContainer = document.getElementById("message-container");
      if (messages.length == 0) {
        messagesContainer.innerHTML = "<p>This user has no posts yet.</p>";
      } else {
        messagesContainer.innerHTML = "";
      }
      messages.forEach(message => {
        const messageDiv = buildMessageDiv(message);
        messagesContainer.appendChild(messageDiv);
      });
    });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement("div");
  headerDiv.classList.add("message-header");
  headerDiv.appendChild(
    document.createTextNode(message.user + " - " + new Date(message.timestamp))
  );

  const bodyDiv = document.createElement("div");
  bodyDiv.classList.add("message-body");
  bodyDiv.innerHTML = message.text;

  const messageDiv = document.createElement("div");
  messageDiv.classList.add("message-div");
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

/** Fetches data and populates the UI of the page. */
window.buildUI = function buildUI() {
  setPageTitle();
  showMessageFormIfViewingSelf();
  fetchMessages();
  fetchAboutMe();
  fetchBlobstoreUrlAndShowForm();
  fetchPlaces();
}

function fetchAboutMe() {
  const url = "/about?user=" + parameterUsername;
  fetch(url)
    .then(response => {
      return response.text();
    })
    .then(aboutMe => {
      const aboutMeContainer = document.getElementById("about-me-container");
      if (aboutMe == "") {
        aboutMe = "This user has not entered any information yet.";
      }
      aboutMeContainer.innerHTML = aboutMe;
    });
}

function fetchBlobstoreUrlAndShowForm() {
  fetch("/api/blobstore-upload-url")
    .then(response => {
      return response.text();
    })
    .then(imageUploadUrl => {
      const messageForm = document.getElementById("message-form");
      messageForm.action = imageUploadUrl;
      messageForm.classList.remove("hidden");
    });
}

function fetchPlaces() {
  fetch("/api/place?user=" + parameterUsername)
    .then(response => {
      return response.json();
    })
    .then(rows => {
      createMap(rows);
      const rootElement = document.getElementById("PinList");
      ReactDOM.render(<PinList rows={rows} />, rootElement);
    });
}

