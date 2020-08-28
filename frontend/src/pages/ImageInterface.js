import React, { useState, Component } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import ImageInterfaceContent from "../main-components/ImageInterfaceContent";

function ImageInterface() {
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
        <ImageInterfaceContent></ImageInterfaceContent>
      </div>
    </div>
  );
}

export default ImageInterface;