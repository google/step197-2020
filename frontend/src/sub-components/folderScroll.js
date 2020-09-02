import React, { Component } from "react";
import styled from "@emotion/styled";

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

class FolderScroll extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      folders: "",
    };
  }

  async componentDidMount() {
    try {
      const folders = await fetch("/userfolders");
      const foldersData = await folders.json();
      if (!folders.ok) {
        throw Error(foldersData.statusText);
      }
      this.setState({ isDataFetched: true, folders: foldersData["userFolders"] });
    } catch (error) {
      alert("Could not load folders, try refreshing page");
    }
  }

  render() {
    if (!this.state.isDataFetched) {
      return <h5>loading</h5>;
    }
    return (
      <Options
        onChange={this.props.clickFunc}
        value={this.props.selected}
        name='folderKey'
        id={this.props.key}>
        {
          // Parses JSON and displays all the users folders
          this.state.folders.map((folder) => {
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