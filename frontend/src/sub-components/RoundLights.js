import React from "react";
import styled from "@emotion/styled";

const Dot = styled.div`
  display: flex;
  height: 6rem;
  width: 6rem;
  border-radius: 50%;
  margin-right: 10%;
`;

class RoundLights extends React.Component {
  constructor(props) {
    super(props);
    this.statusColors = [];
    /**
     * Each rounds color status is appended to the status color array,
     * where green represents completed and gray represents rounds
     * that have not yet been completed.
     */
    for (let i = 0; i < this.props.currentRound; i++) {
      this.statusColors.push("#1aa260");
    }
    const remaining = this.props.totalRounds - this.props.currentRound;
    for (let i = 0; i < remaining; i++) {
      this.statusColors.push("#bbb");
    }
  }

  render() {
    return this.statusColors.map((color, i) => {
      return <Dot style={{ backgroundColor: `${color}` }} key={i}></Dot>;
    });
  }
}

export default RoundLights;