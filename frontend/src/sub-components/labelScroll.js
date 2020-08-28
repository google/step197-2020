import React, { Component } from "react";
import styled from "@emotion/styled";

const container = {
  width: "30%",
  minWidth: "12rem",
  height: "5rem",
  border: "0.2rem solid #136f9f",
  backgroundColor: "white",
  color: "black",
  borderRadius: "0.5rem",
  fontSize: "1.75rem",
};

class LabelScroll extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    if (!this.props.labels) {
      return <p>Loading... Try refreshing the page.</p>;
    }
    const labelItems = this.props.labels.map((label) => {
      return (
        <option key={label} value={label}>
          {label}
        </option>
      );
    });
    return (
      <div
        style={container}
        onChange={this.props.clickFunc}
        value={this.props.selected}
        name="labels"
        id={this.props.name}
      >
        {labelItems}
      </div>
    );
  }
}

export default LabelScroll;
