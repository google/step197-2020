import FlashcardFront from './FlashcardFront';
import React from 'react';
import { Link } from 'react-router-dom';
import newCardIcon from '../SVG/create-24px.svg';

const frontStyle = {
  borderRadius: "30px",
  position: "absolute",
  width: "300px",
  height: "350px",
  backgroundColor: "white",
  color: "black",
  boxShadow: "rgba(0, 0, 0, 0.2) 0px 1px 8px",
  fontFamily: "'Montserrat', sans-serif",
  fontSize: "30px",
  fontWeight: "600",
  textAlign: "center",
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
  alignItems: "center",
};

const imgStyle = {
  display: "block",
  maxWidth: "150px",
  maxHeight: "150px",
  width: "auto",
  height: "auto",
  margin: "10px",
};

const cardStyle = {
  width: '300px',
  height: '350px',
  margin: '15px',
};

/**
* A card that, when clicked on, would lead the user to the /CreateCard page
*/
class NewCard extends React.Component {
  render() {
    return (
      <div style={cardStyle} key='newCard'>
        <Link to='/CreateCard'>
          <div className='FlashcardFront' style={frontStyle}>
            <img style={imgStyle} src={newCardIcon} />
            "+New Card"
          </div>
        </Link>
      </div>
    );
  }
}

export default NewCard;