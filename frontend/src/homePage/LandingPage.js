import React from "react";
import "./LandingPage.css";
import LoginButton from "./LoginButton.js";
import { motion } from "framer-motion";

class LandingPage extends React.Component {
  constructor(props) {
    super(props);
    this.message = "Get Started";
    this.color = "#5A90A1";
    this.clickHandling = this.props.loginClick;
    if (this.props.loginStatus) {
      this.message = "My Folders";
      this.color = "#F4B400";
      this.clickHandling = this.props.folderClick;
    }
  }

  render() {
    return (
      <div className='landingPage'>
        <h1>Frame.Cards learning made easy</h1>
        <motion.div whileHover={{ scale: 1.2 }}>
          <LoginButton
            status={this.message}
            color={this.color}
            fontSize='32px'
            clickFunction={this.clickHandling}></LoginButton>
        </motion.div>
      </div>
    );
  }
}

export default LandingPage;