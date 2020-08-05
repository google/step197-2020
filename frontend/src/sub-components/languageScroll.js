import React, { Component } from "react";
import supportedLang from "./SupportedLang.json";
import styled from "@emotion/styled"

class languageScroll extends Component {
  constructor(props) {
    super(props);
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


    return (
      <Options name="languages"
      >

        {
          // Parse json and display supported languages in scroll list
          supportedLang.languages.map((lang) => {
           
            return <option value={lang.code}>{lang.language}</option>;
          })
        }
      </Options>
    );
  }
}

export default languageScroll;
