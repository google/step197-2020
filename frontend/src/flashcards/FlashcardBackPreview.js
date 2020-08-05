import React from "react";
import styled from "@emotion/styled"

class FlashcardBack extends React.Component {
  render() {
    const Contain = styled.div`
      border-radius: 30px;
      position: relative;
      width: 100%;
      height: 350px;
      background-color: white;
      color: black;
      box-shadow: rgba(0, 0, 0, 0.2) 0px 1px 8px;
      font-family: "Montserrat", sans-serif;
      font-size: 30px;
      font-weight: 600;
      text-align: center;
      display: flex;
      flex-direction: column;
      justify-content: center;
      background-color: #3385e4;
      color: white;
      @media (max-width: 700px) {
        width: 80%;
        height: 70%;
      }
    `;
    return (
      <Contain className="FlashcardBack" >
        {this.props.text}
      </Contain>
    );
  }
}

export default FlashcardBack;
