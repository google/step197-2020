import React from "react";
import styled from "@emotion/styled";
import Flashcard from "../flashcards/Flashcard.js";
import NewCard from "../flashcards/NewCard.js";

const ContainerStyle = {
  flex: "9",
  display: "flex",
  border: "1rem solid white",
  borderRadius: "1rem",
  backgroundColor: "white",
  margin: "1%",
  flexDirection: "column",
};

const CardContainer = {
  display: "flex",
  flexFlow: "row wrap",
  justifyContent: "space-around",
  alignItems: "flex-start",
};

class InsideFolderContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      flashcards: [],
      flashcardCount: 0,
    };
  }

  async componentDidMount() {
    let flashcards = [];
    const fetchPromise = await fetch(
      `/usercards?folderKey=${this.props.folderKey}`
    )
      .then((res) => res.json())
      .then((data) => {
        flashcards = data.userCards.map((flashcard) => (
          <Flashcard
            key={flashcard.key}
            image={flashcard.imageBlobKey}
            text={flashcard.rawText}
            translation={flashcard.textTranslated}
            cardKey={flashcard.key}
            folderKey={this.props.folderKey}
          />
        ));
      })
      .catch((error) => {
        alert("Could not load cards, please try refreshing the page.");
      });
    let count = flashcards?.length;
    flashcards.unshift(<NewCard />);
    this.setState({ flashcards: flashcards, flashcardCount: count, isDataFetched: true });
  }

  render() {
    if (!this.state.isDataFetched) {
      return (
        <div className='loadingContainer'>
          <PageLoading></PageLoading>
        </div>
      );
    }
    return (
      <div style={{ flex: "9", display: "flex" }}>
        <div style={ContainerStyle}>
          <h1>Your Folder has {this.state.flashcardCount} cards.</h1>
          <br />
          <div style={CardContainer}>{this.state.flashcards}</div>
        </div>
      </div>
    );
  }
}

export default InsideFolderContent;
