import React from "react";
import styled from "@emotion/styled";
import Folder from "../flashcards/Folder.js";
import { logDebugMessage } from "./LogDebugMessage";
class MyFoldersContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      words: {},
    };
  }

  async componentDidMount() {
  }

  render() {
    const Container = styled.div`
      flex: 9;
      display: flex;
      border: 1rem solid white;
      border-radius: 1rem;
      background-color: white;
      margin: 1%;
      flex-direction: column;
    `;

  
    return (
      <Container>
        <h1>{headingText}</h1>
        <br></br>
        <div className='wordContainers'>
          {this.state.words.map((word) => {
              return (
                  <h1>{word}</h1>
            );
          })}
        </div>
      </Container>
    );
  }
}

export default MyFoldersContent;
