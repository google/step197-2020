import React from "react";
import styled from "@emotion/styled";
import StudyModeQuiz from "../sub-components/StudyModeQuiz";
import { nextQuizWord, updateWordQueues, getRound } from "./StudyModeGameHandler";

const EndGame = styled.h1`
  color: #d4af37;
  font-size: 10rem;
`;

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
    const word = this.props.quiz.nextQuizWord();
    const round = this.props.quiz.getCurrentRound();
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
    let correct = false;
    if (selectedValue === this.state.correctAnswer) {
      correct = true;
    }

    /*
     * If the selected word was incorrect then this function
     * will repeat this word at the end of another round if possible.
     */
    this.props.quiz.updateWordQueues(correct, this.state.cardKey);
    this.props.quiz.updateWordQueues(correct);
    const word = this.props.quiz.nextQuizWord();
    if (word === null) {
      this.setState({ end: true });
    } else {
      const round = this.props.quiz.getCurrentRound();
      this.setState({
        quizWord: word.quizWord,
        options: word.possibleResponses,
        correctAnswer: word.correctAnswer,
        currentRound: round,
      });
    }
  }

  render() {
    if (this.state.end) {
      return (
        <div className="container">
          <EndGame>You Finished The Game</EndGame>
        </div>
      );
    }
    
    return (
      <StudyModeQuiz
        currentRound={this.state.currentRound}
        totalRounds={this.props.quiz.getTotalRounds()}
        quizWord={this.state.quizWord}
        options={this.state.options}
        optionSelected={this.optionSelected}>
      </StudyModeQuiz>
    );
  }
}

export default StudyModeContent;