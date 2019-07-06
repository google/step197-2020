<!--
Copyright 2019 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
<%@ page import="com.google.codeu.servlets.ServletUtils" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>CodeU 2019 Team 8 Project</title>
    <link rel="stylesheet" href="/css/main.css" />
    <script src="/js/navigation-loader.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=<%= request.getAttribute("MAPS_API_KEY") %>"></script>
    <script src="<%= ServletUtils.BuildReactRoot() %>home.js"></script>
  </head>
  <body onload="addLoginOrLogoutLinkToNavigation(), window.initialize();">
    <nav>
      <ul id="navigation">
        <li><a href="/">Home</a></li>
        <li><a href="/aboutus.html">About Our Team</a></li>
      </ul>
    </nav>
    <h1>PlaceHolder Map of EV Charging Stations</h1>
    <div id="map"></div>
    <div id = "overview">
      <h1>
      Project Overview: 
      </h1>
      <p>
      The user will be able to pin a specific location, search for other users pinned destinations on the map, 
      add new locations, 
      react to other users pinned locations, 
      and view information about a destination.
      </p>
      <p>Sophia: About Me, Images I, Images II, converting About Me to react</p>
      <p>Janet: Public Feed, convert public Feed to React, Style Text</p>
      <p>Thomas: Community Page, convert community page to react, Maps I, Maps II</p>
      </div>
    </div>
    <div id = "PinList"></div>
  </body>
  <style>
    #map {
      width: 500px;
      height: 500px;
      border: thin solid black;
    }
  </style>
</html>
