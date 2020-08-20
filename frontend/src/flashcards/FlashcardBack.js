import React from 'react';

const backStyle = {
	borderRadius: '30px',
	position: 'absolute',
	width: '300px',
	height: '350px',
	boxShadow: 'rgba(0, 0, 0, 0.2) 0px 1px 8px',
	fontFamily: "'Montserrat', sans-serif",
	fontSize: '30px',
	fontWeight: '600',
	textAlign: 'center',
	display: 'flex',
	flexDirection: 'column',
	justifyContent: 'center',
	backgroundColor: '#3385E4',
	color: 'white',
};

function FlashcardBack(props) {
	return (
		<div className="FlashcardBack" style={backStyle}>
			{props.text}
		</div>
	);
}

export default FlashcardBack;
