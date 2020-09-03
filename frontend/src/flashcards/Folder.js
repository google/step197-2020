import React from "react";
import "./Folder.css";
import { Link } from "react-router-dom";

/**
 * A component that represents the Folder Entity. Contains a link to
 * the /insidefolder page that would display the respective cards for
 * the folder.
 */
class Folder extends React.Component {
  constructor(props) {
    super(props);
    this.handleDeleteFolder = this.handleDeleteFolder.bind(this);
  }

  async handleDeleteFolder() {
    const params = new URLSearchParams();
    params.append("folderKey", this.props.folderKey);
    await fetch("/deletefolder", { method: "POST", body: params });
    window.location.reload(false);
  }

  render() {
    return (
      <div className="Folder" key={this.props.folderURL}>
        <Link to={`${this.props.folderURL}?folderKey=${this.props.folderKey}`}>
          <div className="FolderContent">
            <br />
            <p style={{ textDecoration: "underline" }}>{this.props.name}</p>
            <p>{this.props.language}</p>
          </div>
        </Link>
        <div className="icons">
          <Link
            to={{
              pathname: "/EditFolderContent",
              state: {
                name: this.props.name,
                defaultLanguage: this.props.language,
                folderKey: this.props.folderKey,
              },
            }}
          >
            <motion.div whileHover={{ scale: 1.2 }}>
              <img src={editIcon} alt="icon option"></img>
            </motion.div>
          </Link>
          <div className="delete" onClick={this.handleDeleteFolder}>
            <motion.div whileHover={{ scale: 1.2 }}>
              <img src={deleteIcon} alt="icon option"></img>
            </motion.div>
          </div>
        </div>
      </div>
    );
  }
}

export default Folder;
