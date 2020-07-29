import React from "react";
import styled from "@emotion/styled";

const CreateFolderContent = (props) => {
  const Container = styled.div`
    flex: 9;
    display: flex;
    border: 1rem solid white;
    border-radius: 1rem;
    background-color: white;
    margin: 1%;
  `;

  //Code for new page goes here
  return (
    <Container>
      <h1>Creating a new Folder</h1>
    </Container>
  );
};

export default CreateFolderContent;