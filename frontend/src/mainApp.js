import React from 'react';
import ReactDOM from 'react-dom';
import MyFolders from "./pages/MyFolders";
import InsideFolder from "./pages/InsideFolder";
import StudyMode from "./pages/StudyMode";
import CreateCard from "./pages/CreateCard";
import CreateFolder from "./pages/CreateFolder";
import ImageInterface from "./pages/ImageInterface";
import SimilarWords from "./pages/SimilarWords";
import InsideStudyMode from "./pages/InsideStudyMode";
import PageError from "./pages/404";
import css from "./mainApp.css";
import { BrowserRouter, Route, Switch } from "react-router-dom";

// This file handles all routing for our app pages
function App() {
  return (
    <div>
      <Switch>
        <Route exact path='/CreateCard' component={CreateCard} />
        <Route exact path='/MyFolders' component={MyFolders} />
        <Route exact path='/ImageInterface' component={ImageInterface} />
        <Route exact path='/StudyMode' component={StudyMode} />
        <Route exact path='/InsideStudyMode' component={InsideStudyMode} />
        <Route exact path='/CreateFolder' component={CreateFolder} />
        <Route exact path='/InsideFolder' component={InsideFolder} />
        <Route exact path='/SimilarWords' component={SimilarWords} />
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

