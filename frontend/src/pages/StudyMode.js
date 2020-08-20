import React, { useState, Component } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import MyFoldersContent from "../main-components/MyFoldersContent";

function StudyMode() {
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
    <div className="App">
      <Header id="head" handleClick={handleClick}></Header>
      <div id="main">
        <Sidebar bool={sideSetting}></Sidebar>
        <MyFoldersContent
          mainURL="/InsideStudyMode"
          headingText="Select a folder to begin studying">        
        </MyFoldersContent>
      </div>
    </div>
  );
}

export default StudyMode;
