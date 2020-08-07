import React from 'react';
import FlashcardFront from './FlashcardFront';
import { Link } from "react-router-dom";

const folderStyle = {
  borderRadius: "30px",
  position: "absolute",
  width: "300px",
  height: "350px",
  backgroundColor: "white",
  color: "black",
  boxShadow: "rgba(0, 0, 0, 0.2) 0px 1px 8px",
  fontFamily: "'Montserrat', sans-serif",
  fontSize: "30px",
  fontWeight: "600",
  textAlign: "center",
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
  alignItems: "center",
};
const outerStyle = {
  width: "300px",
  height: "350px",
  margin: "10px",
};
class Folder extends React.Component {

  render() {
    return (
      <div className="Folder" key={this.props.key} style={outerStyle}>
        <Link to="/insideFolder">
          <div style={folderStyle}>
            <br />
            <p>{this.props.name}</p>
            <p style={{ textDecoration: "underline" }}>{this.props.language}</p>
          </div>
        </Link>
      </div>
    );
  }
}

export default Folder;