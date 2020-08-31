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

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" name="viewport" content="width=device-width,initial-scale=1"/>
    <title><%= request.getAttribute("TITLE") %></title>
    <!--link rel="stylesheet" href="/css/main.css" /-->
    <%= request.getAttribute("HEAD_HTML") == null ? "" : request.getAttribute("HEAD_HTML") %>
  </head>
  <body>
    <div id="root"></div>
  </body>
    <script crossorigin="anonymous" src="<%= request.getAttribute("SERVER_ROOT") %><%= request.getAttribute("REACT_MODULE") %>.js"></script>
</html>
