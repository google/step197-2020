import React from 'react';
//import logo from './logo.svg';
//import './App.css';
import NavBar from "./mainpage/NavBar";
import LandingPage from "./mainpage/LandingPage";
import About from "./mainpage/About";

function Home() {
  return (
    <div className="App">
      <NavBar/>
      <LandingPage/>
      <About/>
    </div>
  );
}

export default Home;
