import React from 'react';
import logo from './logo.svg';
import './App.css';
import NavBar from './components/NavBar.js';
import LandingPage from './components/LandingPage.js';
import About from './components/About.js';

function App() {
  return (
    <div className="App">
      <NavBar/>
      <LandingPage/>
      <About/>
    </div>
  );
}

export default App;
