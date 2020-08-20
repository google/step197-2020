import React, { useState, Component, useEffect } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import queryString from "query-string";

function InsideStudyMode(props) {
  // Handles mobile menu button and updates sidebar view
  const [sideSetting, setSideSetting] = useState("f");
  const handleClick = (e) => {
    console.log("Clicked");
    if (sideSetting === "f") {
      setSideSetting("t");
    } else {
      setSideSetting("f");
    }
  };

  return (
    <div className='App'>
      <Header id='head' handleClick={handleClick}></Header>
      <div id='main'>
        <Sidebar bool={sideSetting}></Sidebar>
        <h1>Inside Study Mode</h1> 
      </div>
    </div>
  );
}

export default InsideStudyMode;
