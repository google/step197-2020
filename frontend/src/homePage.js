import React, { useState, Component } from "react";
import ReactDOM from "react-dom";
import NavBar from "./homePage/NavBar";
import LandingPage from "./homePage/LandingPage";
import About from "./homePage/About";

class Home extends React.Component {
  constructor(props) {
    super(props);
    this.loginClick = this.handleLoginClick.bind(this);
    this.folderClick = this.handleFoldersClick.bind(this);
    this.state = {
      userKey: "null",
      loginStatus: false,
      logoutUrl: "null",
      isDataFetched: false,
    };
    this.checkLogin();
  }

  // Calls the login Servlet when the page loads for the first time
  checkLogin(e) {
    const userInfo = fetch("/login")
      .then((response) => response.json())
      .then((info) => {
        if (info["showTabStatus"] === true) {
          this.setState({ loginStatus: true, logoutUrl: info["logoutUrl"], isDataFetched: true, userKey: info['userInfo']['userKey'] });
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

        // If the user has logged in before then the button will now be a logout button
        if (info["showTabStatus"] === true) {
          window.location = this.state.logoutUrl;
        } else {
          window.location = info["loginUrl"];
        }
      });
  };


  handleFoldersClick = (e) => {
    window.location = "/myFolders?userKey=" + this.state.userKey;
  };

  render() {
    // Ensures that the loginServlet has been called before loading child components 
    if (!this.state.isDataFetched) return null;
    return (
      <div className="App">
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
        <About />
      </div>
    );
  }
}

ReactDOM.render(<Home />, document.getElementById("root"));

