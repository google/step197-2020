import React, { useState, useEffect } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import SimilarWordsContent from "../main-components/SimilarWordsContent";
import queryString from "query-string";

function MyFolders(props) {
  // Handles mobile menu button and updates sidebar view
   const [sidebarVisibility, setSidebarVisibility] = useState(false);
   const handleClick = (e) => {
     setSidebarVisibility((sidebarVisibility) => !sidebarVisibility);
   };

  const values = queryString.parse(props.location.search);
  const word = values.queryWord.toLowerCase();
  const numWords = values.numOfWordsRequested;

  return (
    <div className='App'>
      <Header id='head' handleClick={handleClick}></Header>
      <div id='main'>
        <Sidebar visible={sidebarVisibility}></Sidebar>
        <SimilarWordsContent
          word={word}
          numWords={numWords}></SimilarWordsContent>
      </div>
    </div>
  );
}

export default MyFolders;