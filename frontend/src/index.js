import React from 'react';
import ReactDOM from 'react-dom';
import css from "./index.css";
import App from './App';
import { BrowserRouter } from "react-router-dom";
/*
  <BrowserRouter>
    <App />
  </BrowserRouter>,
*/
ReactDOM.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>,
  document.getElementById('root')
);

