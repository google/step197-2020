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
      cardKey: "",
    };
    this.optionSelected = this.optionSelected.bind(this);
  }

  componentDidMount() {
    // Grabs the first word in the game
    const word = nextQuizWord();
    const round = getRound();
    this.setState({
      quizWord: word.quizWord,
      options: word.possibleResponses,
      correctAnswer: word.correctAnswer,
      cardKey: word.cardKey,
      currentRound: round
    });
  }

  optionSelected(event) {
    const selectedValue = event.currentTarget.value;
    let correct = "false";
    
    if (selectedValue === this.state.correctAnswer) {
      correct = "true";
    }

    /**
     * If the selected word was incorrect then this function
     * will repeat this word at the end of another round if possible.
     */
    updateWordQueues(correct, this.state.cardKey);
    const word = nextQuizWord();

    if (word === null) {
      this.setState({ end: true });
    }

    const round = getRound();
    this.setState({
       quizWord: word.quizWord,
       options: word.possibleResponses,
       correctAnswer: word.correctAnswer,
       cardKey: word.cardKey,
       currentRound: round
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