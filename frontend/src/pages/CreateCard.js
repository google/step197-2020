import React from "react";
import CreateCardContent from "../main-components/CreateCardContent";
import ContentWithSidebar from "./ContentWithSidebar";
import queryString from "query-string";

function CreateCard(props) {
  let text = "";
  const values = queryString.parse(props.location.search);
  if (values.word !== undefined) {
    text = values.word;
  }
  return (
    <ContentWithSidebar>
      <CreateCardContent word={text}></CreateCardContent>
    </ContentWithSidebar>
  );
}

export default CreateCard;