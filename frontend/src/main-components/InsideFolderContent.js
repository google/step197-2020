import React from "react";
import styled from "@emotion/styled";
import Flashcard from "../flashcards/Flashcard.js";
import NewCard from "../flashcards/NewCard.js";

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

  //Ideally we would use a servlet to get cards, but for now here's an empty card container.
  const cardsContainer = [
    {
      blobKey: String,
      labels: String,
      fromLang: String,
      toLang: String,
      textNotTranslated: String,
      textTranslated: String,
      cardKey: String,
    },
    {
      blobKey: String,
      labels: String,
      fromLang: String,
      toLang: String,
      textNotTranslated: "Hi",
      textTranslated: "Hola",
      cardKey: String,
    },
    {
      blobKey: String,
      labels: String,
      fromLang: String,
      toLang: String,
      textNotTranslated: String,
      textTranslated: String,
      cardKey: String,
    },
    {
      blobKey: String,
      labels: String,
      fromLang: String,
      toLang: String,
      textNotTranslated: String,
      textTranslated: String,
      cardKey: String,
    },
  ];

  //Get the card
  const flashcards = cardsContainer.map((flashcard) => (
    <Flashcard
      key={flashcard.cardKey}
      image={flashcard.blobKey}
      text={flashcard.textNotTranslated}
      translation={flashcard.textTranslated}
      labels={flashcard.labels}
    />
  ));
  const CardContainer = {
    display: "flex",
    flexFlow: "row wrap",
    justifyContent: "space-around",
    alignItems: "start",
  };

  let count = flashcards.length;
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
