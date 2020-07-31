import React from "react";
import ReactDOM from "react-dom";
import NavBar from "./mainpage/NavBar";
import LandingPage from "./mainpage/LandingPage";
import About from "./mainpage/About";

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
