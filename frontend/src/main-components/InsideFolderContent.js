import React from 'react';
import styled from '@emotion/styled';
import Flashcard from '../flashcards/Flashcard.js';
import NewCard from '../flashcards/NewCard.js';
import { LogDebugMessage } from './LogDebugMessage.js';

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
	};
  }

  componentDidMount() {
	let flashcards=[];
	const FetchPromise = fetch(`/usercards?folderKey=${this.props.folderKey}`, { method: 'GET' });
	FetchPromise.then((result) => result.json())
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
	  })
	  .catch((error) => {
		alert("Could not load cards, please try refreshing the page.");
	});
	let count = flashcards?.length;
	flashcards.unshift(<NewCard />);
	console.log(count);
	this.setState({ flashcards: flashcards, flashcardCount: count });
  }
  
  render(){
	return (
	  <div style={{ flex: '9', display: 'flex' }}>
		  <div style={ContainerStyle}>
			<h1>Your Folder has {this.state.flashcardCount} cards.</h1>
			<br/>
			<div style={CardContainer}>
				{this.state.flashcards}
			</div>
		  </div>
	  </div>
	);
  }
}

export default InsideFolderContent;
