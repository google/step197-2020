import React from "react";
import CreateFolderContent from "../main-components/CreateFolderContent";
import ContentWithSidebar from "./ContentWithSidebar";

function CreateFolder() {
  return (
    <ContentWithSidebar>
      <CreateFolderContent></CreateFolderContent>
    </ContentWithSidebar>
  );
}

export default CreateFolder;