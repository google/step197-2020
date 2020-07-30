import React, { useState, Component } from "react";
//import "./App.css";
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Link,
  Redirect,
} from "react-router-dom";
import MyFolders from "./pages/MyFolders";
import InsideFolder from "./pages/InsideFolder";
import YoutubeDetect from "./pages/YoutubeDetect";
import CreateCard from "./pages/CreateCard";
import CreateFolder from "./pages/CreateFolder";
import ImageInterface from "./pages/ImageInterface";
import PageError from "./pages/404";

// This file handles all routing to different pages
function App() {
  return (
    <div className="App">
      <Switch>
        <Route exact path="/CreateCard" component={CreateCard} />
        <Route exact path="/" component={MyFolders} />
        <Route exact path="/ImageInterface" component={ImageInterface} />
        <Route exact path="/YoutubeInterface" component={YoutubeDetect} />
        <Route exact path="/CreateFolder" component={CreateFolder} />
        <Route exact path="/InsideFolder" component={InsideFolder} />
        <Route component={PageError}/>
      </Switch>
    </div>
  );
}

export default App;
