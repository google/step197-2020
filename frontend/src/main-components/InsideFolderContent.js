import React from 'react';
import styled from '@emotion/styled';
import Flashcard from '../flashcards/Flashcard.js';
import NewCard from '../flashcards/NewCard.js';
import { LogDebugMessage } from './LogDebugMessage.js';

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

	let flashcards = [];
	const FetchPromise = fetch(`/usercards?folderKey=${props.folderKey}`, { method: 'GET' });
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
			LogDebugMessage('Can not fetch flashcards.');
		});

	const CardContainer = {
		display: 'flex',
		flexFlow: 'row wrap',
		justifyContent: 'space-around',
		alignItems: 'start',
	};

	const count = flashcards?.length;

	flashcards.unshift(<NewCard />);
	return (
		<div style={{ flex: '9', display: 'flex' }}>
			<Container>
				<h1>Your Folder has {count} cards</h1>
				<br></br>
				<div style={CardContainer}>{flashcards}</div>
			</Container>
		</div>
	);
};

export default InsideFolderContent;
