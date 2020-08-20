import React, { useState } from "react";
import Header from "../main-components/Header";
import Sidebar from "../main-components/Sidebar";
import StudyModeContent from "../main-components/StudyModeContent";
import css from "./template.css";

class InsideStudyMode extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      rounds: 4,
    };
  }
  // Initializes a new game
    async componentDidMount() {
      
  }

  // Must clear game incase of early termination
    componentWillUnmount() { }
    
  render() {
    // Handles mobile menu button and updates sidebar view
    const [sideSetting, setSideSetting] = useState("f");
    const handleClick = (e) => {
      console.log("Clicked");
      if (sideSetting === "f") {
        setSideSetting("t");
      } else {
        setSideSetting("f");
      }
    };
    if (!isDataFetched) {
      return (
        <div className='App'>
          <Header id='head' handleClick={handleClick}></Header>
          <div id='main'>
            <Sidebar bool={sideSetting}></Sidebar>
          </div>
        </div>
      );
    }
    return (
      <div className='App'>
        <Header id='head' handleClick={handleClick}></Header>
        <div id='main'>
          <Sidebar bool={sideSetting}></Sidebar>
          <StudyModeContent totalRounds={this.state.rounds}></StudyModeContent>
        </div>
      </div>
    );
  }
}

export default InsideStudyMode;
