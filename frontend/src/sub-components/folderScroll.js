import React, { Component, useState } from "react";
import supportedLang from "./SupportedLang.json";
import styled from "@emotion/styled";

class FolderScroll extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      folders: "",
    };
    this.grabFolders();
  }

  grabFolders() {
    const folders = fetch("/folder").then((data) =>
      useState({ isDataFetched: true, folders: data })
    );
  }

  render() {
    const Options = styled.select`
      width: 30%;
      min-width: 12rem;
      height: 5rem;
      border: 0.2rem solid #136f9f;
      background-color: white;
      color: black;
      border-radius: 0.5rem;
      font-size: 1.75rem;
    `;

    return (
      <Options
        onChange={this.props.clickFunc}
        value={this.props.selected}
        name="languages"
      >
        {
          // Parses JSON and displays all the users folders
          data.usersFolders.map((folder) => {
            return (
              <option key={folder.folderName} value={folder.folderKey}>
                {folder.folderName}
              </option>
            );
          })
        }
      </Options>
    );
  }
}

export default FolderScroll;
