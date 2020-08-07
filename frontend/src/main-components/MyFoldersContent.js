import React from "react";
import styled from "@emotion/styled";
import Folder from "../flashcards/Folder.js";

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
  }
  const folders = folderData.map((folder) =>
    <Folder
      key={folder.folderKey}
      name={folder.folderName}
      language={folder.folderDefaultLanguage} />
  );

  return (
    <Container>
      <h1>You have 0 Folders </h1>
      <br></br>
      <div className="folderContainer" style={folderStyle}>
        {folders}
      </div>
    </Container>
  );
};

export default MyFoldersContent;