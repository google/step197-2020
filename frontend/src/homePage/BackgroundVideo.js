import React from "react";
import clip from "../SVG/clip.webm"
import styled from "@emotion/styled";

const Container = styled.div`
  display: flex;
  justify-content: center;
`;

const Video = styled.video`
  margin: 5rem;
  width: 85%;
  border: .3rem solid black;
  border-radius: .5rem;
`;

class Background extends React.Component {
  render() {
    return (
      <Container>
        <Video autoPlay="autoplay" loop="loop" muted >
          <source src={clip} type="video/webm" />
          Your browser does not support the video tag.
        </Video>
      </Container>
    )
  }
}

export default Background;