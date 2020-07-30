import React from 'react';
//import './NavBar.css';
import LoginButton from './LoginButton.js';

class NavBar extends React.Component {
    constructor(props) {
      super(props);
    }
  
    render() {
      return (
        <div className="NavBar">
          <div className="Logo"></div>
          <LoginButton status="Login" color="#C4C4C4" fontSize="24px"></LoginButton>
        </div>
      );
    }
  }

export default NavBar;

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
//serviceWorker.unregister();