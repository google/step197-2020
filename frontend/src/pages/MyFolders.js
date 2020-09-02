import React, { useEffect } from "react";
import MyFoldersContent from "../main-components/MyFoldersContent";
import queryString from "query-string";
import ContentWithSidebar from "./ContentWithSidebar";

function MyFolders(props) {
  let userKey;
  useEffect(() => {
    const values = queryString.parse(props.location.search);
    userKey = values.userKey;
  });

  return (
    <ContentWithSidebar>
      <MyFoldersContent
        userKey={userKey}
        mainURL='/InsideFolder'
        headingText='main'>
      </MyFoldersContent>
    </ContentWithSidebar>
  );
}

export default MyFolders;