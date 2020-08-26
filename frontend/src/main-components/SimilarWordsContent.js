import React from "react";
import styled from "@emotion/styled";

const Container = styled.div`
  flex: 9;
  display: flex;
  border: 1rem solid white;
  border-radius: 1rem;
  background-color: white;
  margin: 1%;
  flex-direction: column;
`;

const WordContainer = styled.div`
  margin-top: 2%;
  display: flex;
  width: 100%;
  justify-content: space-evenly;
  flex-wrap: wrap;
`;

const Word = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 3%;
  width: 22%;
  margin-right: 5%;
  margin-left: 5%;
  background-color: white;
  border-radius: 4rem;
  box-shadow: rgba(255, 153, 0, 0.9) 0px 1px 8px;
  font-size: 4rem;
`;

class MyFoldersContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      words: [],
      error: false,
      errorMessage: "",
    };
  }

  async componentDidMount() {
    let searchParams = new URLSearchParams();
    searchParams.append("queryWord", this.props.word);
    searchParams.append("numOfWordsRequested", this.props.numWords);
    try {
      const response = await fetch("/recommendation?" + searchParams.toString())
        .then((res) => res.json());
      if ("error" in response) {
        this.setState({ error: true, errorMessage: response.error });
      }
      this.setState({ isDataFetched: true, words: response[this.props.word] });
    } catch (err) {
      alert("Could not load, refresh page");
    }
  }

  render() {
    if (!this.state.isDataFetched) {
      return <Container></Container>;
    }
    if (this.state.error) {
      return (
        <Container>
          <h1>{this.state.errorMessage}</h1>
        </Container>
      );
    }
    return (
      <Container>
        <WordContainer>
          {this.state.words.map((word) => {
            return <Word key={word}>{word}</Word>;
          })}
        </WordContainer>
      </Container>
    );
  }
}

export default MyFoldersContent;