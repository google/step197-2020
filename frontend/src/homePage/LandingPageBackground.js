import React from "react";
import "./LandingPage.css";
import LoginButton from "./LoginButton.js";
import { motion } from "framer-motion";
import styled from "@emotion/styled";
import { init } from "./BouncingLetters";

const Canvas = styled.canvas`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
`;

class Background extends React.Component {
  componentDidMount() {
    init();
  }

  render() {
    return <Canvas id='myCanvas'></Canvas>;
  }
}

export default Background;