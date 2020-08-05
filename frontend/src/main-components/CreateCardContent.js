import React, { Component } from "react";
import styled from "@emotion/styled";
import css from "./CreateCardContent.css";
import { motion } from "framer-motion";
import FrontCard from "../flashcards/FlashcardFrontPreview"
import BackCard from "../flashcards/FlashcardBackPreview";
import translate, { getTranslation } from "../sub-components/translate"
import LangaugeScroll from "../sub-components/languageScroll"
class CreateCardContent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      imgSrc: "",
      text: "none",
      translation: "none",
      fromLang: "none",
      toLang: "none",
    };
    this.TranslateText = this.TranslateText.bind(this);
    getTranslation("good day", "en", "es");
  }
  /* 
  * When the user has finished typing the Google translate  
  * API iss called to fetch the translated version    of text
 */
  TranslateText(event) {
    event.preventDefault();
    if (this.state.fromLang !== "none" && this.state.toLang !== "none") {
      const translated = translate(event.target.value, this.state.fromLang, this.state.toLang);
      this.setState({ translation: translated, text: event.target.value});
    }
    this.setState({ text: event.target.value });
  }

  render() {
    return (
      <div id="container">
        <div id="innerContainer">
          <div id="CardPreview">
            <div className="holdCard">
              <FrontCard
                image={this.state.imgSrc}
                text={this.state.text}
              ></FrontCard>
            </div>
            <div className="holdCard">
              <BackCard text={this.state.translation}></BackCard>
            </div>
          </div>

          <div id="formBox">
            <ul>
              <form id="myForm" action="/usercards">
                <li>
                  <span className="inline">
                    <label className="block">From:</label>
                    <LangaugeScroll key="from"></LangaugeScroll>
                  </span>
                  <span className="inline">
                    <label className="block">To:</label>
                    <LangaugeScroll key="to"></LangaugeScroll>
                  </span>
                </li>
                <li>
                  <label className="block">Text:</label>
                  <input id="mainText" type="text" placeholder={this.state.text} onBlur={this.TranslateText}required></input>
                </li>
                <li>
                  <label className="block">Translation:</label>
                  <input id="translate" type="text" value={this.state.translation} readOnly></input>
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
