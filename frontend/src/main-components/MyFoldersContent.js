import React from "react";
import styled from "@emotion/styled";
import Folder from "../flashcards/Folder.js";
import { logDebugMessage } from "./LogDebugMessage";
import PageLoading from "../sub-components/PageLoading";

const Container = styled.div`
  flex: 9;
  display: flex;
  border: 1rem solid white;
  border-radius: 1rem;
  background-color: white;
  margin: 1%;
  flex-direction: column;
`;

const folderStyle = {
  display: "flex",
  flexFlow: "row wrap",
  justifyContent: "space-around",
  alignItems: "start",
};

class MyFoldersContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      folders: "",
    };
  }

  async componentDidMount() {
    const foldersData = await fetch("/userfolders")
      .then((result) => result.json())
      .then((data) => data["userFolders"]);
    this.setState({ isDataFetched: true, folders: foldersData }).catch(
      (error) => {
        alert("Could not load cards, please try refreshing the page.");
      }
    );
  }

  render() {
    if (!this.state.isDataFetched) {
      return (
        <div className='loadingContainer'>
          <PageLoading></PageLoading>
        </div>
      );
    }

    let headingText =
      "You have no folders at the moment. Please make a new folder by clicking on the side menu.";
    if (this.state.folders) {
      if (this.props.headingText === "main") {
        headingText = `You have ${this.state.folders.length} Folders`;
      } else {
        headingText = this.props.headingText;
      }
    }

    return (
      <Container>
        <h1>{headingText}</h1>
        <br></br>
        <div className="folderContainer" style={folderStyle}>
          {this.state.folders.map((folder) => {
            return (
              <Folder
                folderURL={this.props.mainURL}
                folderKey={folder.folderKey}
                name={folder.folderName}
                language={folder.folderDefaultLanguage}
                key={folder.folderKey}
              />
            );
          })}
        </div>
      </Container>
    );
  }
}

export default MyFoldersContent;
