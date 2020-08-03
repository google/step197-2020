import React from 'react';
import FlashcardFront from './FlashcardFront';
import FlashcardBack from './FlashcardBack';
import ReactCardFlip from 'react-card-flip';

/*
  This is the styling for the main flashcard.
*/
const flipStyle ={
  width:"300px",
  height:"350px",
  margin:"10px",
};

/*
  The Flashcard component takes in three props:
  text, translation, and image.
  It renders a flashcard with both a front and a back.
  It also uses the react-card-flip library, so make 
  sure to install it if possible!
*/
class Flashcard extends React.Component {
    constructor(props) {
      super(props);
      this.handleClick=this.handleClick.bind(this);
      this.state = { isFlipped: false};
    }
    
    /*
      This allows the flashcard to be flipped over
      upon being clicked on.
    */
    handleClick(e) {
      e.preventDefault();
      this.setState(prevState => ({ isFlipped: !prevState.isFlipped }));
      console.log(e);
    }

    render() {
      return (
        <div className="Flashcard" onClick={this.handleClick} style={flipStyle}>
            <ReactCardFlip 
              isFlipped={this.state.isFlipped} 
              flipDirection="horizontal" 
              containerStyle={flipStyle}
              cardZIndex="-2">
                <FlashcardFront text={this.props.text} image={this.props.image}/>
                <FlashcardBack text={this.props.translation}/>
            </ReactCardFlip>
        </div>
      );
    }
  }

export default Flashcard;