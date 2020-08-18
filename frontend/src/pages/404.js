import React from "react";
import  styled from "@emotion/styled"
import Sidebar from "../main-components/Sidebar";
import CreateCardContent from "../main-components/CreateCardContent";

function PageError() {
    const Container = styled.div`
        background-color:white;
    `;
  return (
    <Container>
      <Sidebar></Sidebar>
      <h2>404 page not found</h2>
    </Container>
  );
}

export default PageError;
