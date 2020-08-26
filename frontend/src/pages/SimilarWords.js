import React, { useState, useEffect } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import SimilarWordsContent from "../main-components/SimilarWordsContent";
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

    let word;
    let numWords;
  (() => {
    const values = queryString.parse(props.location.search);
    word = values.word;
    numWords = values.numWords;
  });

  return (
    <div className='App'>
      <Header id='head' handleClick={handleClick}></Header>
      <div id='main'>
        <Sidebar bool={sideSetting}></Sidebar>
        <SimilarWordsContent></SimilarWordsContent>
      </div>
    </div>
  );
}

export default MyFolders;
