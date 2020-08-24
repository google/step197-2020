import React from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import StudyModeContent from "../main-components/StudyModeContent";
import { startQuiz } from "../main-components/StudyModeGameHandler";
import css from "./template.css";
import queryString from "query-string";

class InsideStudyMode extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      rounds: 0,
      sideSetting: "f",
    };
    this.handleClick = this.handleClick.bind(this);
  }

  // Initializes a new game
  async componentDidMount() {
    const parameters = queryString.parse(props.location.search);
    const rounds = await startQuiz(parameters.folderKey);
    this.setState({ rounds, isDataFetched: true });
  }

  handleClick(event) {
    if (sideSetting === "f") {
      this.setState({ sideSetting: "t" });
    } else {
      this.setState({ sideSetting: "f" });
    }
  };

  render() {
    // Handles mobile menu button and updates sidebar view
    if (!this.state.isDataFetched) {
      return (
        <div className='App'>
          <Header id='head' handleClick={this.handleClick}></Header>
          <div id='main'>
            <Sidebar bool={this.state.sideSetting}></Sidebar>
          </div>
        </div>
      );
    }
    return (
      <div className='App'>
        <Header id='head' handleClick={this.handleClick}></Header>
        <div id='main'>
          <Sidebar bool={this.state.sideSetting}></Sidebar>
          <StudyModeContent totalRounds={this.state.rounds}></StudyModeContent>
        </div>
      </div>
    );
  }
}

export default InsideStudyMode;