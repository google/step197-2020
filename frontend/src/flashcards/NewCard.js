import FlashcardFront from './FlashcardFront';
import React from 'react';
import { Link } from "react-router-dom";
import newCardIcon from "../SVG/create-24px.svg"

const cardStyle = {
    width:"300px",
    height:"350px",
    margin:"10px",
  };
  
class NewCard extends React.Component {

    render(){
        return(
            <div style={cardStyle}>
                <Link to="/CreateCard">
                    <FlashcardFront image={newCardIcon} text="+New Card"></FlashcardFront>
                </Link>
            </div>
        );
    }
}
export default NewCard;