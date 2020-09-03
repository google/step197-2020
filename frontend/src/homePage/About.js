import React from 'react';
import './About.css';
import Background from "./BackgroundVideo";

class About extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
      }
    }
  
    render() {
      return (
        <div className="about">
          <Background></Background>
        </div>
      );
    }
  }

export default About;