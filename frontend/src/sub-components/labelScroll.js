import React, { Component } from "react";
import styled from "@emotion/styled";

const container={
    width: "30%",
    minWidth: "12rem",
    height: "5rem",
    border: "0.2rem solid #136f9f",
    backgroundColor: "white",
    color: "black",
    borderRadius: "0.5rem",
    fontSize: "1.75rem",
}
class LabelScroll extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isDataFetched: false,
      labels: [],
    };
  }

  async componentDidMount() {
    try {
      const labels = await fetch("/userfolders").then((result) => result.json());
      if (!labels.ok) {
        throw Error(labels.statusText);
      }
      this.setState({ isDataFetched: true, labels: labels });
    } catch (error) {
      alert("Could not fetch labels, please try refreshing the page.");
    }
  }

  render() {
    const Options = styled.select`
      width: 30%;
      min-width: 12rem;
      height: 5rem;
      border: 0.2rem solid #136f9f;
      background-color: white;
      color: black;
      border-radius: 0.5rem;
      font-size: 1.75rem;
    `;
    if (!this.state.isDataFetched) {
      return <h5>loading</h5>;
    }
    return (
      <div style={container}
        onChange={this.props.clickFunc}
        value={this.props.selected}
        name='labels'
        id={this.props.key}>
        {
          data.labels.map((label) => {
            return (
              <option key={label} value={label}>
                {label}
              </option>
            );
          })
        }
      </div>
    );
  }
}

export default LabelScroll;