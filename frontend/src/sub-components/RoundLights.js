import React from "react";
import styled from "@emotion/styled";

const Dot = styled.div`
  display: flex;
  height: 6rem;
  width: 6rem;
  border-radius: 50%;
  margin-right: 10%;
`;

function RoundLights(props)  {
    let statusColors = [];
    /*
     * Each rounds color status is appended to the status color array,
     * where green represents completed and gray represents rounds
     * that have not yet been completed.
     */
    for (let i = 0; i < props.currentRound; i++) {
      statusColors.push("#1aa260");
    }
    const remaining = props.totalRounds - props.currentRound;
    for (let i = 0; i < remaining; i++) {
      statusColors.push("#bbb");
    }

    return statusColors.map((color, i) => {
      return <Dot style={{ backgroundColor: `${color}` }} key={i}></Dot>;
    });
}

export default RoundLights;