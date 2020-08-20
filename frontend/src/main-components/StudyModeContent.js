import React from "react";
import StudyModeQuiz from "../sub-components/StudyModeQuiz";
import { nextQuizWord, updateWordQueues, getRound } from "./StudyModeGameHandler";

class StudyModeContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentRound: 1,
      quizWord: "none",
      options: ["hello", "hi", "hola", "bonjour"],
      correctAnswer: "none",
    };
    this.cardKey = "";
    this.optionSelected = this.optionSelected.bind(this);
  }

  componentDidMount() {
     const word = nextQuizWord();
     const round = getRound();
     this.setState({ quizWord: word.quizWord,
       options: word.possibleResponses, correctAnswer: word.correctAnswer,
       currentRound: round
     });
  }

  optionSelected(event) {
    const selectedValue = event.currentTarget.value;
    let correct = "false";
    if (selectedValue === this.state.correctAnswer) {
      correct = "true";
    }
    updateWordQueues(correct)
     const word = nextQuizWord();
     const round = getRound();
     this.setState({
       quizWord: word.quizWord,
       options: word.possibleResponses,
       correctAnswer: word.correctAnswer,
       currentRound: round,
     });
  }

  render() {
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
