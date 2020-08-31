import React, { useState } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import StudyModeContent from "../main-components/StudyModeContent";
import css from "./template.css";

function InsideStudyMode(props) {
  // Handles mobile menu button and updates sidebar view
 const [sidebarVisibility, setSidebarVisibility] = useState(false);
 const handleClick = (e) => {
   setSidebarVisibility((sidebarVisibility) => !sidebarVisibility);
 };

  return (
    <div className='App'>
      <Header id='head' handleClick={handleClick}></Header>
      <div id='main'>
        <Sidebar visible={sidebarVisibility}></Sidebar>
        <StudyModeContent></StudyModeContent>
      </div>
    </div>
  );
}

export default InsideStudyMode;