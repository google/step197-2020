import React from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import StudyModeContent from "../main-components/StudyModeContent";
import { Quiz } from "../main-components/StudyModeGameHandler";
import css from "./template.css";
import queryString from "query-string";

class InsideStudyMode extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      quiz: {},
      sideSetting: false,
    };
    this.handleClick = this.handleClick.bind(this);
  }

  // Initializes a new game
  async componentDidMount() {
    const parameters = queryString.parse(props.location.search);
    const quiz = Quiz(); 
    await quiz.start(parameters.folderKey);
    this.setState({ quiz, isDataFetched: true });
  }

  handleClick(event) {
    this.setState((prevState) => {
      return { sideSetting: !prevState.sideSetting };
    })
  }

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
          <StudyModeContent Quiz={this.state.quiz}></StudyModeContent>
        </div>
      </div>
    );
  }
}

export default InsideStudyMode;