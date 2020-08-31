import React, { useState, Component } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import styled from "@emotion/styled";
import CreateCardContent from "../main-components/CreateCardContent";
import css from "./template.css"

function CreateCard() {
  // Handles mobile menu button and updates sidebar view
  const [sidebarVisibility, setSidebarVisibility] = useState(false);
  const handleClick = (e) => {
    setSidebarVisibility(sidebarVisibility => !sidebarVisibility);
  };
    
    return (
      <div className="App">
        <Header id="head" handleClick={handleClick}></Header>
        <div id="main">
          <Sidebar bool={sidebarVisibility}></Sidebar>
          <CreateCardContent></CreateCardContent>
        </div>
      </div>
    );
  
}

export default CreateCard;