import React from "react";
import SimilarWordsContent from "../main-components/SimilarWordsContent";
import queryString from "query-string";
import ContentWithSidebar from "./ContentWithSidebar";

function MyFolders(props) {
  const values = queryString.parse(props.location.search);
  const word = values.queryWord;
  const numWords = values.numOfWordsRequested;

  return (
    <ContentWithSidebar>
      <SimilarWordsContent
        word={word}
        numWords={numWords}>
      </SimilarWordsContent>
    </ContentWithSidebar>
  );
}

export default MyFolders;