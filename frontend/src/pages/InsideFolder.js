import React, { useState, Component, useEffect } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import InsideFolderContent from "../main-components/InsideFolderContent";
import queryString from "query-string";

function InsideFolder(props) {
  const [sidebarVisibility, setSidebarVisibility] = useState(false);
  const handleClick = (e) => {
    setSidebarVisibility((sidebarVisibility) => !sidebarVisibility);
  };

  const values = queryString.parse(props.location.search);
  const folderKey = values.folderKey;

  return (
    <div className='App'>
      <Header id='head' handleClick={handleClick}></Header>
      <div id='main'>
        <Sidebar bool={sidebarVisibility}></Sidebar>
        <InsideFolderContent folderKey={folderKey}></InsideFolderContent>
      </div>
    </div>
  );
}

export default InsideFolder;