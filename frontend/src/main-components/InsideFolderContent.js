import React from "react";
import styled from "@emotion/styled";
import Flashcard from "../flashcards/Flashcard.js";
import NewCard from "../flashcards/NewCard.js";

const DebugMessage = (message, isOn) => {
  if (isOn) {
    console.log(message);
  }
};

const debuggerMessageOn = false;

const InsideFolderContent = (props) => {
  const Container = styled.div`
    flex: 9 1 auto;
    display: flex;
    border: 1rem solid white;
    border-radius: 1rem;
    background-color: white;
    margin: 1%;
    flex-direction: column;
  `;

  let flashcards;
  try {
    fetch(`/usercards?folderKey=${folderKey}`, { method: "GET" })
      .then((result) => result.json())
      .then((data) => {
        flashcards = data.map((flashcard) => (
          <Flashcard
            key={flashcard.cardKey}
            image={flashcard.blobKey}
            text={flashcard.rawText}
            translation={flashcard.textTranslated}
            labels={flashcard.labels}
          />
        ));
      });
  } catch (err) {
    DebugMessage("Can not fetch flashcards", debuggerMessageOn);
  }

  const CardContainer = {
    display: "flex",
    flexFlow: "row wrap",
    justifyContent: "space-around",
    alignItems: "start",
  };
  let count = 0;

  if (flashcards) {
    count = flashcards.length;
  }
  flashcards.unshift(<NewCard />);
  return (
    <div style={{ flex: "9", display: "flex" }}>
      <Container>
        <h1>Your Folder has {count} cards</h1>
        <br></br>
        <div style={CardContainer}>{flashcards}</div>
      </Container>
    </div>
  );
};

export default InsideFolderContent;
