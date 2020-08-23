import React from "react";
import styled from "@emotion/styled";
import StudyModeQuiz from "../sub-components/StudyModeQuiz";
import { nextQuizWord, updateWordQueues, getRound } from "./StudyModeGameHandler";

class StudyModeContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentRound: 1,
      quizWord: "none",
      options: [],
      correctAnswer: "none",
      end: false,
    };
    this.optionSelected = this.optionSelected.bind(this);
  }

  componentDidMount() {
    const word = nextQuizWord();
    const round = getRound();
    this.setState({
      quizWord: word.quizWord,
      options: word.possibleResponses,
      correctAnswer: word.correctAnswer,
      currentRound: round
     });
  }

  optionSelected(event) {
    const selectedValue = event.currentTarget.value;
    let correct = "false";
    if (selectedValue === this.state.correctAnswer) {
      correct = "true";
    }
    updateWordQueues(correct);
    const word = nextQuizWord();
    if (word === null) {
      this.setState({ end: true });
    }
    const round = getRound();
    this.setState({
      quizWord: word.quizWord,
      options: word.possibleResponses,
      correctAnswer: word.correctAnswer,
      currentRound: round,
     });
  }

  render() {
    const EndGame = styled.h1`
        color: #D4AF37;
        font-size: 10rem;
    `;

    if (this.state.end) {
      return (
        <div className="Container">
          <EndGame>You Finished The Game</EndGame>
        </div>
      );
    }
    
    return (
      <StudyModeQuiz
        currentRound={this.state.currentRound}
        totalRounds={this.props.totalRounds}
        quizWord={this.state.quizWord}
        options={this.state.options}
        optionSelected={this.optionSelected}>
      </StudyModeQuiz>
    );
  }
}

export default StudyModeContent;
