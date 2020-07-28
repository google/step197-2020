import React from "react";
import  styled from "@emotion/styled"

function PageError() {
    const Container = styled.div`
        background-color:white;
    `;
  return (
    <Container>
      <h2>404 page not found</h2>
    </Container>
  );
}

export default PageError;
