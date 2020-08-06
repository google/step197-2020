import React from "react";
import styled from "@emotion/styled";

const MyFoldersContent = (props) => {
  const Container = styled.div`
    flex: 9;
    display: flex;
    border: 1rem solid white;
    border-radius: 1rem;
    background-color: white;
    margin: 1%;
  `;
  const folderData =[
    {
      folderName: String,
      folderDefaultLanguage: String,
      folderKey: String
    },
    {
      folderName: String,
      folderDefaultLanguage: String,
      folderKey: String
    },
 ]
 const folders = folderData.map((folder) =>

);
  
  return (
    <Container>
      <h1>You have 0 Folders </h1>
      <br></br>
      <div style={FolderContainer}>{folders}</div>
    </Container>
  );
};

export default MyFoldersContent;