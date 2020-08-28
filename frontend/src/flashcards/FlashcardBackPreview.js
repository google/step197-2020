import React from "react";
import styled from "@emotion/styled";

 const FlashcardContainer = styled.div`
   background-color: #3385e4;
   border-radius: 30px;
   box-shadow: rgba(0, 0, 0, 0.2) 0px 1px 8px;
   color: white;
   display: flex;
   font-family: "Montserrat", sans-serif;
   font-size: 30px;
   font-weight: 600;
   flex-direction: column;
   height: 350px;
   justify-content: center;
   position: relative;
   text-align: center;
   width: 100%;
   @media (max-width: 700px) {
     height: 100%;
     width: 100%;
   }
 `;
 
class FlashcardBack extends React.Component {
  render() {

    return (
      <FlashcardContainer className="FlashcardBack">
        {this.props.text}
      </FlashcardContainer>
    );
  }
}

export default FlashcardBack;
