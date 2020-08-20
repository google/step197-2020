import React, { useState, Component, useEffect } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import MyFoldersContent from "../main-components/MyFoldersContent";
import queryString from "query-string";

function MyFolders(props) {
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

  let userKey;
  useEffect(() => {
    const values = queryString.parse(props.location.search);
    userKey = values.userKey;
  });

  return (
    <div className="App">
      <Header id="head" handleClick={handleClick}></Header>
      <div id="main">
        <Sidebar bool={sideSetting}></Sidebar>
        <MyFoldersContent userKey={userKey}
          mainURL="/InsideFolder"
          headingText="main">
        </MyFoldersContent>
      </div>
    </div>
  );
}

export default MyFolders;
