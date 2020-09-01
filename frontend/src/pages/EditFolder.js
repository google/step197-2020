import React, { useState, Component } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import styled from "@emotion/styled";
import EditFolderContent from "../main-components/EditFolderContent";

function EditFolder(props) {
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
        <EditFolderContent
          defaultLanguage={props.location.state.defaultLanguage}
          name={props.location.state.name}
          folderKey={props.location.state.folderKey}
        ></EditFolderContent>
      </div>
    </div>
  );
}

export default EditFolder;
