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

class FlashcardFront extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

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
      align-items: center;
      @media (max-width: 700px) {
          width: 80%;
          height: 70%;
      }
    `;
    return (
      <Contain className="FlashcardFront">
        <img src={this.props.image} alt="image" style={imgStyle} />
        {this.props.text}
      </Contain>
    );
  }
}
/*<img src={this.props.image}/>*/
export default FlashcardFront;
