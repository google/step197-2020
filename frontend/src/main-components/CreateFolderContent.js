import React, { Component } from "react";
import styled from "@emotion/styled";
import css from "./CreateFolderContent.css";
import { motion } from "framer-motion";
import supportedLang from "../sub-components/SupportedLang.json";

class CreateFolderContent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      folderName: "None",
      folderLang: "English",
    };
    // TODO: get userKey
    this.userKey ="ahFzdTE5LWNvZGV1LTgtNTQ3MnIfCxIEVXNlciIVMTcxNzQ5MTA2ODUxNTcxMzcxNTkxDA";
    this.handleFolderName = this.handleFolderName.bind(this);
    this.handleFolderLang = this.handleFolderLang.bind(this);
  }

  handleFolderName(event) {
    event.preventDefault();
    console.log(this.userKey);
    this.setState({ folderName: event.target.value });
  }

  handleFolderLang(event) {
    this.setState({ folderLang: event.target.value });
  }

  /**
   * This react component renders a form that makes a post
   * request to UserFoldersServlet for folder creation.
   */
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
              <form id="myForm" action={`/folder?userKey=${this.userKey}`} method="POST">
                <li>
                  <label>Folder Name: </label>
                </li>
                <li>
                  <input
                    id="folderName"
                    name="folderName"
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
                  <div id="scroll">
                    <select
                      id="language"
                      name="language"
                      value={this.state.folderLang}
                      onChange={this.handleFolderLang}
                      required
                    >
                      <option value="English">English</option>
                      {
                        // Parse json and display supported languages in scroll list
                        supportedLang.languages.map((lang) => {
                          return (
                            <option value={lang.language}>
                              {lang.language}
                            </option>
                          );
                        })
                      }
                    </select>
                  </div>
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
