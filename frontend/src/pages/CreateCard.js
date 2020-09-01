import React from "react";
import CreateCardContent from "../main-components/CreateCardContent";
import ContentWithSidebar from "./ContentWithSidebar";

function CreateCard() {
    return (
      <ContentWithSidebar>
        <CreateCardContent></CreateCardContent>
      </ContentWithSidebar>
    );
  
}

export default CreateCard;