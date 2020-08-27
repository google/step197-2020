import React, { useState, Component } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import CreateFolderContent from "../main-components/CreateFolderContent";

function CreateFolder() {
  // Handles mobile menu button and updates sidebar view
   const [sideSetting, setSideSetting] = useState(false);
   const handleClick = (e) => {
     setSideSetting((sideSetting) => !sideSetting);
   };

  return (
    <div className="App">
      <Header id="head" handleClick={handleClick}></Header>
      <div id="main">
        <Sidebar bool={sideSetting}></Sidebar>
        <CreateFolderContent></CreateFolderContent>  
      </div>
    </div>
  );
}

export default CreateFolder;