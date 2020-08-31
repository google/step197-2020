import React, { useState } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import CreateCardContent from "../main-components/CreateCardContent";
import queryString from "query-string";

function CreateCard(props) {
  // Handles mobile menu button and updates sidebar view
  const [sideBarVisibility, setSideBarVisibility] = useState(false);
  const handleClick = (e) => {
    setSideBarVisibility((sideBarVisibility) => !sideBarVisibility);
  };

  let text = "";
  const values = queryString.parse(props.location.search);
  if (values.word !== undefined) {
    text = values.word;
  }

  return (
    <div className='App'>
      <Header id='head' handleClick={handleClick}></Header>
      <div id='main'>
        <Sidebar visible={sideBarVisibility}></Sidebar>
        <CreateCardContent word={text}></CreateCardContent>
      </div>
    </div>
  );
}

export default CreateCard;