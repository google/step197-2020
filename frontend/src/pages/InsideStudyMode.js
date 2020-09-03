import React from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import StudyModeContent from "../main-components/StudyModeContent";
import { Quiz, StudyService } from "../main-components/StudyModeGameHandler";
import css from "./template.css";
import queryString from "query-string";
import PageLoading from "../sub-components/PageLoading";

class InsideStudyMode extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      quiz: {},
      sidebarVisibility: false,
    };
    this.handleClick = this.handleClick.bind(this);
  }

  // Initializes a new game
  async componentDidMount() {
    const parameters = queryString.parse(this.props.location.search);
    const quiz = new Quiz(new StudyService());
    await quiz.start(parameters.folderKey);
    if (quiz.getTotalRounds() === 0) {
      alert("No cards were found");
      window.location = "/MyFolders";
    }
    this.setState({ quiz, isDataFetched: true });
  }

  handleClick(event) {
    this.setState((prevState) => {
      return { sidebarVisibility: !prevState.sidebarVisibility };
    });
  }

  render() {
    // Handles mobile menu button and updates sidebar view
    if (!this.state.isDataFetched) {
      return (
        <div className='App'>
          <Header id='head' handleClick={this.handleClick}></Header>
          <div id='main'>
            <Sidebar visible={this.state.sidebarVisibility}></Sidebar>
            <div className='loadingContainer'>
              <PageLoading></PageLoading>
            </div>
          </div>
        </div>
      );
    }

    return (
      <div className='App'>
        <Header id='head' handleClick={this.handleClick}></Header>
        <div id='main'>
          <Sidebar visible={this.state.sidebarVisibility}></Sidebar>
          <StudyModeContent quiz={this.state.quiz}></StudyModeContent>
        </div>
      </div>
    );
  }

}

export default InsideStudyMode;