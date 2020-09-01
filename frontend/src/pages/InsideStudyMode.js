import React from "react";
import StudyModeContent from "../main-components/StudyModeContent";
import ContentWithSidebar from "./ContentWithSidebar";

function InsideStudyMode(props) {
  return (
    <ContentWithSidebar>
      <StudyModeContent></StudyModeContent>
    </ContentWithSidebar>
  );
}

export default InsideStudyMode;