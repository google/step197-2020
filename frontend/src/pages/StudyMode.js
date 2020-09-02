import React from "react";
import MyFoldersContent from "../main-components/MyFoldersContent";
import ContentWithSidebar from "./ContentWithSidebar";

function StudyMode() {
  return (
    <ContentWithSidebar>
      <MyFoldersContent
        mainURL='/InsideStudyMode'
        headingText='Select a folder to begin studying'>
      </MyFoldersContent>
    </ContentWithSidebar>
  );
}

export default StudyMode;