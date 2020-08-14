import React from 'react';

const frontStyle = {
	borderRadius: '30px',
	position: 'absolute',
	width: '300px',
	height: '350px',
	backgroundColor: 'white',
	color: 'black',
	boxShadow: 'rgba(0, 0, 0, 0.2) 0px 1px 8px',
	fontFamily: "'Montserrat', sans-serif",
	fontSize: '30px',
	fontWeight: '600',
	textAlign: 'center',
	display: 'flex',
	flexDirection: 'column',
	justifyContent: 'center',
	alignItems: 'center',
};

const imgStyle = {
	display: 'block',
	maxWidth: '150px',
	maxHeight: '150px',
	width: 'auto',
	height: 'auto',
	margin: '10px',
};

class FlashcardFront extends React.Component {
	constructor(props) {
		super(props);
		this.state = {};
	}

	render() {
		return (
			<div className="FlashcardFront" style={frontStyle}>
				<img src={this.props.image} alt={this.props.text} style={imgStyle} />
				{this.props.text}
			</div>
		);
	}
}
export default FlashcardFront;
