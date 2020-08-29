import React, { useState, useEffect } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import SimilarWordsContent from "../main-components/SimilarWordsContent";
import queryString from "query-string";

function MyFolders(props) {
  // Handles mobile menu button and updates sidebar view
   const [sideBarVisibility, setSideBarVisibility] = useState(false);
   const handleClick = (e) => {
     setSideBarVisibility((sideBarVisibility) => !sideBarVisibility);
   };

  const values = queryString.parse(props.location.search);
  const word = values.queryWord;
  const numWords = values.numOfWordsRequested;

  return (
    <div className='App'>
      <Header id='head' handleClick={handleClick}></Header>
      <div id='main'>
        <Sidebar bool={sideBarVisibility}></Sidebar>
        <SimilarWordsContent
          word={word}
          numWords={numWords}></SimilarWordsContent>
      </div>
    </div>
  );
}

export default MyFolders;