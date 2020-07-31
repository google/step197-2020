import React from "react";
import ReactDOM from "react-dom";
import NavBar from "./homePage/NavBar";
import LandingPage from "./homePage/LandingPage";
import About from "./homePage/About";

function Home() {
  return (
    <div className="App">
      <NavBar />
      <LandingPage />
      <About />
    </div>
  );
}

ReactDOM.render(
    <Home></Home>,
  document.getElementById("root")
);
