import React, { Component } from "react";
import styled from "@emotion/styled";
import css from "./CreateFolderContent.css";
import { motion } from "framer-motion";

class CreateFolderContent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      folderName: "None",
      folderLang: "English",
    };
    // TODO: get userId
    this.userId = "123";
    this.handleFolderName = this.handleFolderName.bind(this);
    this.handleFolderLang = this.handleFolderLang.bind(this);
  }

  handleFolderName(event) {
    event.preventDefault();
    this.setState({ folderName: event.target.value });
  }

  handleFolderLang(event) {
    this.setState({ folderLang: event.target.value });
  }


  render() {
    
    return (
      <div id="container">
        <div id="innerContainer">
          <div id="folderPreview">
            <h2 id="previewFolderName">{this.state.folderName}</h2>
            <h4 id="previewFolderLang">{this.state.folderLang}</h4>
          </div>

          <div id="formBox">
            <ul>
              <form id="myForm" action="/folder">
                <li>
                  <label>Folder Name: </label>
                </li>
                <li>
                  <input
                    id="folderType"
                    type="text"
                    placeholder={this.state.folderName}
                    onBlur={this.handleFolderName}
                    required
                  ></input>
                </li>

                <li>
                  <label>Folder Language:</label>
                </li>
                <li>
                  <select
                    id="folderLang"
                    value={this.state.folderLang}
                    onChange={this.handleFolderLang}
                  >
                    <option value="English">English</option>
                    <option value="Spanish">Spanish</option>
                    <option value="Chinese">Chinese</option>
                  </select>
                </li>
                <li>
                  <input id="submission" type="submit" value="Submit" />
                </li>
              </form>
            </ul>
          </div>
        </div>
      </div>
    );
  }
}

export default CreateFolderContent;
