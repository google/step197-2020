import React from 'react';
import ReactDOM from 'react-dom';
import MyFolders from "./pages/MyFolders";
import InsideFolder from "./pages/InsideFolder";
import YoutubeDetect from "./pages/YoutubeDetect";
import CreateCard from "./pages/CreateCard";
import CreateFolder from "./pages/CreateFolder";
import ImageInterface from "./pages/ImageInterface";
import PageError from "./pages/404";
import css from "./mainApp.css";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { match } from "react-router-dom"

// This file handles all routing for our app pages
function App() {
  return (
    <div>
      <Switch>
        <Route exact path="/createCard" component={CreateCard} />
        <Route exact path="/myFolders" component={MyFolders} />
        <Route exact path="/imageInterface" component={ImageInterface} />
        <Route exact path="/youtubeInterface" component={YoutubeDetect} />
        <Route exact path="/createFolder" component={CreateFolder} />
        <Route exact path="/insideFolder" component={InsideFolder} />
        <Route component={PageError} />
      </Switch>
    </div>
  );
}

ReactDOM.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>,
  document.getElementById('root')
);

