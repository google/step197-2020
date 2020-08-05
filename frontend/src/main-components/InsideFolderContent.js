import React from "react";
import styled from "@emotion/styled";
import Flashcard from "../flashcards/Flashcard.js";
import NewCard from "../flashcards/NewCard.js";


const InsideFolderContent = (props) => {
  const Container = styled.div` 
    flex: 9;
    display: flex;
    border: 1rem solid white;
    border-radius: 1rem;
    background-color: white;
    margin: 1%;
  `;
  
  

  //Ideally we would call datastore to get the cards
  const cardsContainer= [{
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
  ]
  
  //Get the card
  const flashcards = cardsContainer.map((flashcard) =>
      <Flashcard
      key={flashcard.cardKey} 
      image={flashcard.blobKey}
      text={flashcard.textNotTranslated} 
      translation={flashcard.textTranslated}
      labels={flashcard.labels} />
  );
  const CardContainer= {
    width:"90%",
    display:"flex",
    flexFlow: "row wrap"
    
    };
  let count = flashcards.length;
  flashcards.unshift(<NewCard/>);
  return (
    <div>
      <Container>
        <h1>Your Folder has {count} cards</h1>
        <br></br>
        <div style={CardContainer}>
          {flashcards}
        </div>
      </Container>
    </div>
  );
};

export default InsideFolderContent;
