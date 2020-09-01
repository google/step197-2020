import React, { useState, Component } from "react";
import ReactDOM from "react-dom";
import NavBar from "./homePage/NavBar";
import LandingPage from "./homePage/LandingPage";
import About from "./homePage/About"

class Home extends React.Component {
  constructor(props) {
    super(props);
    this.loginClick = this.handleLoginClick.bind(this);
    this.folderClick = this.handleFoldersClick.bind(this);
    this.state = {
      loginStatus: false,
      logoutUrl: "null",
      isDataFetched: false,
    };
    this.checkLogin();
  }

  // Calls the login servlet to check if the user is logged in
  checkLogin(e) {
    const userInfo = fetch("/login")
      .then((response) => response.json())
      .then((info) => {
        if (info["showTabStatus"] === true) {
          this.setState({
            loginStatus: true,
            logoutUrl: info["logoutUrl"],
            isDataFetched: true,
          });
        } else {
          this.setState({ loginStatus: false, isDataFetched: true });
        }
      });
  }

  // Handles buttons that login & logout user
  handleLoginClick = (e) => {
    const userInfo = fetch("/login")
      .then((response) => response.json())
      .then((info) => {
        // If the user is logged in then the button will become a logout button
        if (info["showTabStatus"] === true) {
          window.location = this.state.logoutUrl;
        } else {
          window.location = info["loginUrl"];
        }
      });
  };

  handleFoldersClick = (e) => {
    window.location = "/MyFolders";
  };

  render() {
    // Ensures that the loginServlet has been called before loading child components
    if (!this.state.isDataFetched) return null;
    return (
      <div className='App'>
        <NavBar
          loginStatus={this.state.loginStatus}
          loginClick={this.loginClick}
          folderClick={this.folderClick}
        />
        <LandingPage
          loginStatus={this.state.loginStatus}
          loginClick={this.loginClick}
          folderClick={this.folderClick}
          Url={this.handleLoginClick}
        />
        <About></About>
      </div>
    );
  }
}

ReactDOM.render(<Home />, document.getElementById("root"));