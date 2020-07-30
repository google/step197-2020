import React from 'react';
//import './LoginButton.css';

class LoginButton extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
      }
    }
  
    render() {
      return (
        <div className="LoginButton" style={{
          backgroundColor: this.props.color,
          fontSize: this.props.fontSize,
          }}>
          <a href="#" target="_blank">{this.props.status}</a>
        </div>
      );
    }
  }

export default LoginButton;