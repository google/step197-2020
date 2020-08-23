import React from 'react';
import styled from '@emotion/styled';
import Flashcard from '../flashcards/Flashcard.js';
import NewCard from '../flashcards/NewCard.js';
import { LogDebugMessage } from './LogDebugMessage.js';

const ContainerStyle = {
  flex: '9',
  display: 'flex',
  border: '1rem solid white',
  borderRadius: '1rem',
  backgroundColor: 'white',
  margin: '1%',
  flexDirection: 'column',
};

const CardContainer = {
  display: 'flex',
  flexFlow: 'row wrap',
  justifyContent: 'space-around',
  alignItems: 'baseline',
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
    const fetchPromise = await fetch(`/usercards?folderKey=${this.props.folderKey}`)
      .then(res => res.json())
      .then((data) => {
        console.log(data);
        flashcards = data.userCards.map((flashcard) => (
	      <Flashcard
            key={flashcard.key}
            image={flashcard.blobKey}
            text={flashcard.rawText}
            translation={flashcard.textTranslated}
          />
        ));
      })
      .catch((error) => {
        alert('Could not load cards, please try refreshing the page.');
      });
    let count = flashcards?.length;
    flashcards.unshift(<NewCard />);
    this.setState({ flashcards: flashcards, flashcardCount: count });
  }

  render() {
    return (
	  <div style={{ flex: '9', display: 'flex' }}>
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
