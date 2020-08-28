import React, { Component } from "react";
import supportedLang from "./SupportedLang.json";
import styled from "@emotion/styled";

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

class LanguageScroll extends Component {
  render() {
    return (
      <Options
        onChange={this.props.clickFunc}
        value={this.props.selected}
        name='languages'>
        {supportedLang.languages.map((lang) => {
          return (
            <option key={lang.code} value={lang.code}>
              {lang.language}
            </option>
          );
        })}
      </Options>
    );
  }
}

export default LanguageScroll;