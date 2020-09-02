import React from "react";
import InsideFolderContent from "../main-components/InsideFolderContent";
import queryString from "query-string";
import ContentWithSidebar from "./ContentWithSidebar";

function InsideFolder(props) {
  const values = queryString.parse(props.location.search);
  const folderKey = values.folderKey;

  return (
    <ContentWithSidebar>
      <InsideFolderContent folderKey={folderKey}></InsideFolderContent>
    </ContentWithSidebar>
  );
}

export default InsideFolder;