import React from "react";
import styled from "@emotion/styled";


const InsideFolderContent = (props) => {
  const Container = styled.div`
    flex: 9;
    display: flex;
    border: 1rem solid white;
    border-radius: 1rem;
    background-color: white;
    margin: 1%;
  `;


  return (
    <Container>
      <h1>Your Folder has 0 cards</h1>
    </Container>
  );
};

export default InsideFolderContent;
