import React from "react";
import "./Folder.css";
import { Link } from "react-router-dom";

class Folder extends React.Component {
  render() {
    return (
      <div className="Folder" key={this.props.folderURL}>
        <Link to={`/insideFolder?folderKey=${this.props.folderURL}`}>
          <div className="FolderContent">
            <br />
            <p style={{ textDecoration: "underline" }}>{this.props.name}</p>
            <p>{this.props.language}</p>
          </div>
        </Link>
      </div>
    );
  }
}

export default Folder;
