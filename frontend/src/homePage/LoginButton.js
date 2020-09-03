import React from "react";
import "./LoginButton.css";

class LoginButton extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="buttonBox">
        <button
          className="myButton"
          onClick={this.props.clickFunction}
          style={{
            backgroundColor: this.props.color,
            fontSize: this.props.fontSize,
          }}
        > {this.props.status} </button>
      </div>
    );
  }
}

export default LoginButton;