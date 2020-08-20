import React from "react";
import RoundLights from "../sub-components/RoundLights";
import FlashcardBack from "../flashcards/FlashcardBack";
import Options from "../sub-components/CardOptions";
import css from "./StudyModeContent.css";

class StudyModeContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      currentRound: 1,
      totalRounds: 4,
    };
    this.optionSelected = this.optionSelected.bind(this);
  }

  optionSelected(event) {
    const selectedValue = event.currentTarget.value;
    // TODO(esaracay): Check if the word was correct
  }

  render() {
    return (
      <div id='studyContainer' className='Container'>
        <div id='rounds'>
          <RoundLights
            currentRound={this.state.currentRound}
            totalRounds={this.state.totalRounds}></RoundLights>
        </div>

        <div id='studyMain'>
          <div id='card'>
            <FlashcardBack text={"testing"}></FlashcardBack>
          </div>

          <div id='options'>
            <Options
              options={["hello", "hola", "bonjour", "hi"]}
              func={this.optionSelected}></Options>
          </div>
        </div>
      </div>
    );
  }
}

export default StudyModeContent;