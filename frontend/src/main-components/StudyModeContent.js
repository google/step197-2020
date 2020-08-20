import React from "react";
import StudyModeQuiz from "../sub-components/StudyModeQuiz";

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

  optionSelected(event) {
    const selectedValue = event.currentTarget.value;
    let correct = "false";
    if (selectedValue === this.state.correctAnswer) {
      correct = "true";
    }
    // TODO(esaracay): fetch with parameters
    const response = fetch('/study',"method:POST")
  }

  render() {
    return (
      <StudyModeQuiz
        currentRound={this.state.currentRound}
        totalRounds={this.props.totalRounds}
        quizWord={this.state.quizWord}
        options={this.state.options}
      optionSelected={this.optionSelected}></StudyModeQuiz>
    );
  }
}

export default StudyModeContent;
