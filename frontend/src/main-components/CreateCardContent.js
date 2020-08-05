import React, { Component } from "react";
import styled from "@emotion/styled";
import css from "./CreateCardContent.css";
import { motion } from "framer-motion";
import FrontCard from "../flashcards/FlashcardFrontPreview"
import BackCard from "../flashcards/FlashcardBackPreview";
import LangaugeScroll from "../sub-components/languageScroll"
class CreateCardContent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      imgSrc: "",
      text: "none",
      translation:"none",
    };
  }

  handleText(event) {
    event.preventDefault();
    this.setState({ folderName: event.target.value });
  }

  render() {
    return (
      <div id="container">
        <div id="innerContainer">
          <div id="CardPreview">
            <div class="holdCard">
              <FrontCard
                image={this.state.imgSrc}
                text={this.state.text}
              ></FrontCard>
            </div>
            <div class="holdCard">
              <BackCard text={this.state.translation}></BackCard>
            </div>
          </div>

          <div id="formBox">
            <ul>
              <form id="myForm" action="/usercards">
                <li>
                  <span className="inline">
                    <label className="block">From:</label>
                    <LangaugeScroll></LangaugeScroll>
                  </span>
                  <span className="inline">
                    <label className="block">To:</label>
                    <LangaugeScroll></LangaugeScroll>
                  </span>
                </li>
                <li>
                  <label className="block">Text:</label>
                 =
                </li>
                <li>
                  <label className="block">Translation:</label>
                  <p id="translated"></p>
                </li>
                <li>
                  <span className="inline">
                    <label className="block">Image:</label>
                    <input type="file" id="image" name="imageSelect"></input>
                  </span>
                  <span className="inline">
                    <label className="block">Folder:</label>
                    <input type="file" id="folder" name="folderSelect"></input>
                  </span>
                </li>

                <li>
                  <input id="submission" type="submit" value="Submit" />
                </li>
              </form>
            </ul>
          </div>
        </div>
      </div>
    );
  }
}

export default CreateCardContent;
