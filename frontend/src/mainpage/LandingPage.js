import React from 'react';
import './LandingPage.css';
import LoginButton from './LoginButton.js';

class LandingPage extends React.Component {
    constructor(props) {
      super(props);
    }
  
    render() {
      return (
        <div className="LandingPage">
          <h1>Frame.cards learning made easy</h1>
          <LoginButton status="Get Started" color="#5A90A1" fontSize="32px"></LoginButton>
        </div>
      );
    }
  }

export default LandingPage;