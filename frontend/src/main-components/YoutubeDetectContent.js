import React from "react";
import styled from "@emotion/styled";

const YoutubeDetectContent = (props) => {
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
     <h1>Youtube Detection</h1>
    </Container>
  );
};

export default YoutubeDetectContent;
