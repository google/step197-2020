import React from 'react';
import './LandingPage.css';
import LoginButton from './LoginButton.js';
import { motion } from "framer-motion";

class LandingPage extends React.Component {
    constructor(props) {
      super(props);
    }
  
    render() {
      return (
        <div className="LandingPage">
          <h1>Frame.cards learning made easy</h1>
          <motion.div whileHover={{scale:1.2}}>
            <LoginButton
              status="Get Started"
              color="#5A90A1"
              fontSize="32px"
            ></LoginButton>
          </motion.div>
        </div>
      );
    }
  }

export default LandingPage;