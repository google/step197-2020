import React from "react";
import styled from "@emotion/styled";
import Folder from "../flashcards/Folder.js";

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
    let headingText =
      "You have no folders at the moment. Please make a new folder by clicking on the side menu.";
    if (!this.state.isDataFetched) {
      return <h1>loading</h1>;
    }
    if (this.state.folders) {
      headingText = `You have ${this.state.folders.length} Folders`;
    }
    return (
      <Container>
        <h1>{headingText}</h1>
        <br></br>
        <div className="folderContainer" style={folderStyle}>
          {this.state.folders.map((folder) => {
            return (
              <Folder
                folderURL={folder.folderKey}
                name={folder.folderName}
                language={folder.folderDefaultLanguage}
                key={folder.folderKey}
                folderKey={folder.folderKey}/>
            );
          })}
        </div>
      </Container>
    );
  }
}

export default MyFoldersContent;
