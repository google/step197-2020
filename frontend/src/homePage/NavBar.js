import React, {Component} from "react";
import css from "./NavBar.css";
import LoginButton from "./LoginButton.js";
import {motion} from "framer-motion"

class NavBar extends React.Component {
  constructor(props) {
    super(props);
    this.html = null;
    this.message = "Login";
    // Check if the user is logged in to display folders
    if (this.props.loginStatus) {
      this.html = (
        <LoginButton
          status="My Folders"
          color="#F4B400"
          fontSize="24px"
          clickFunction={this.props.folderClick}
        ></LoginButton>
      );
      this.message = "Logout";
    }
  }

  render() {
    return (
      <div className="navBar">
        <motion.div whileHover={{ scale: 1.1 }}>{this.html}</motion.div>
        <motion.div whileHover={{ scale: 1.1 }}>
          <LoginButton
            status={this.message}
            clickFunction={this.props.loginClick}
            color="#C4C4C4"
            fontSize="24px"
          ></LoginButton>
        </motion.div>
      </div>
    );
  }
}

export default NavBar;