import React from "react";
import styled from "@emotion/styled";
import Folder from "../flashcards/Folder.js";

const debugMessage = (message, isOn) => {
  if(isOn){
    console.log(message);
  }
};

const debugMessageOn = false;

const MyFoldersContent = (props) => {
  const Container = styled.div`
    flex: 9;
    display: flex;
    border: 1rem solid white;
    border-radius: 1rem;
    background-color: white;
    margin: 1%;
    flex-direction: column;
  `;
  const folderStyle = {
    display: "flex",
    flexFlow: "row wrap",
    justifyContent: "space-around",
    alignItems: "start",
  };

  let folders;

  //Fetch data from the UserFolders servlets to get the user folders
  try {
    fetch(`/userfolders?userKey=${props.userKey}`, { method: "GET" })
      .then((result) => result.json())
      .then((data) => {
        folders = data.map((folder) => (
          <Folder
            folderURL={folder.folderKey}
            name={folder.folderName}
            language={folder.folderDefaultLanguage}
            key={folder.folderKey}
          />
        ));
      });
  } catch (err) {
    debugMessage("Folders can not be fetched", debugMessageOn);
  }

  let headingText =
    "You have no folders at the moment. Please make a new folder by clicking on the side menu.";
  if (folders) {
    headingText = `You have ${folders.length} Folders`;
  }

  return (
    <Container>
      <h1>{headingText}</h1>
      <br></br>
      <div className="folderContainer" style={folderStyle}>
        {folders}
      </div>
    </Container>
  );
};

export default MyFoldersContent;
