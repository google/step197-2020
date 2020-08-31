import React from "react";
import styled from "@emotion/styled";

/*
  Styling for the image inside the flashcard
*/
const imgStyle = {
  display: "block",
  maxWidth: "150px",
  maxHeight: "150px",
  width: "auto",
  height: "auto",
  margin: "10px",
};

 const Contain = styled.div`
   align-items: center;
   background-color: white;
   border-radius: 30px;
   box-shadow: rgba(0, 0, 0, 0.2) 0px 1px 8px;
   color: black;
   display: flex;
   flex-direction: column;
   font-family: "Montserrat", sans-serif;
   font-size: 30px;
   font-weight: 600;
   height: 350px;
   position: relative;
   width: 100%;
   text-align: center;
   justify-content: center;
   @media (max-width: 700px) {
     height: 100%;
     width: 100%;
   }
 `;

class FlashcardFront extends React.Component {
  render() {
    
    return (
      <Contain className="FlashcardFront">
        <img src={this.props.image} alt="image" style={imgStyle} />
        {this.props.text}
      </Contain>
    );
  }
}

export default FlashcardFront;
