import React from 'react';
import FlashcardFront from './FlashcardFront';
import FlashcardBack from './FlashcardBack';
import ReactCardFlip from 'react-card-flip';
import deleteIcon from '../SVG/delete-24px.svg';
import editIcon from '../SVG/edit-24px.svg';
import { motion } from 'framer-motion';

/**
 * This is the styling for the main flashcard.
 */
const flipStyle = {
	width: '300px',
	height: '350px',
};
const iconStyle = {
	display: 'flex',
	justifyContent: 'flex-end',
	paddingTop: '1%',
};
const imageStyle = {
	paddingLeft: '2%',
};
/**
 * The Flashcard component renders a flipping
 * flashcard with both a front and a back.
 * It also uses the react-card-flip library, so make
 * sure to install it if possible!
 */
class Flashcard extends React.Component {
	constructor(props) {
		super(props);
		this.handleClick = this.handleClick.bind(this);
		this.state = { isFlipped: false };
		this.handleDeleteCard = this.handleDeleteCard.bind(this);
		this.handleEditCard = this.handleEditCard.bind(this);
	}

	/**
	 * handleClick allows the flashcard to be flipped over
	 * upon being clicked on.
	 */
	handleClick(e) {
		this.setState((prevState) => ({ isFlipped: !prevState.isFlipped }));
	}

	handleDeleteCard(e) {}

	handleEditCard(e) {}

	render() {
		return (
			<div
				className="Flashcard"
				onClick={this.handleClick}
				style={{ height: '350px', width: '300px', margin: '15px' }}
			>
				<ReactCardFlip
					isFlipped={this.state.isFlipped}
					flipDirection="horizontal"
					containerStyle={flipStyle}
					cardZIndex="-2"
				>
					<FlashcardFront text={this.props.text} image={this.props.image} />
					<FlashcardBack text={this.props.translation} />
				</ReactCardFlip>
				<div className="icons" style={iconStyle}>
					<div className="edit" onClick={this.handleEditCard}>
						<motion.div whileHover={{ scale: 1.2 }}>
							<img style={imageStyle} src={editIcon} alt="icon option"></img>
						</motion.div>
					</div>
					<div className="delete" onClick={this.handleDeleteCard}>
						<motion.div whileHover={{ scale: 1.2 }}>
							<img style={imageStyle} src={deleteIcon} alt="icon option"></img>
						</motion.div>
					</div>
				</div>
			</div>
		);
	}
}

export default Flashcard;
