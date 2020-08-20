import React from "react";
import RoundLights from "./RoundLights";
import FlashcardBack from "../flashcards/FlashcardBack";
import Options from "./CardOptions";
import css from "./StudyModeQuiz.css";

class StudyModeQuiz extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div id='studyContainer' className='Container'>
        <div id='rounds'>
          <RoundLights
            currentRound={this.props.currentRound}
            totalRounds={this.props.totalRounds}></RoundLights>
        </div>

        <div id='studyMain'>
          <div id='card'>
            <FlashcardBack text={this.props.quizWord}></FlashcardBack>
          </div>

          <div id='options'>
            <Options
              options={this.props.options}
              func={this.props.optionSelected}></Options>
          </div>
        </div>
      </div>
    );
  }
}

export default StudyModeQuiz;
