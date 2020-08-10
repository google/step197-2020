import React, { Component } from "react";
import styled from "@emotion/styled";
import css from "./CreateCardContent.css";
import { motion } from "framer-motion";
import FrontCard from "../flashcards/FlashcardFrontPreview";
import BackCard from "../flashcards/FlashcardBackPreview";
import { getTranslation } from "../sub-components/translate";
import LangaugeScroll from "../sub-components/languageScroll";
import FolderScroll from "../sub-components/folderScroll";
class CreateCardContent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      imgSrc: "",
      text: "none",
      translation: "none",
      fromLang: "none",
      toLang: "none",
      folder: "",
    };
    this.TranslateText = this.TranslateText.bind(this);
    this.fromLangSelected = this.fromLangSelected.bind(this);
    this.toLangSelected = this.toLangSelected.bind(this);
    // TODO: Implement a way for the userKey to be passed between all pages for now it will be hardcoded
    this.userKey =
      "ahFzdTE5LWNvZGV1LTgtNTQ3MnIfCxIEVXNlciIVMTIxODQwMTczNDMzMDE4NzUzNDMxDA";
  }
  /**
   * When the user has finished typing the Google translate
   * API iss called to fetch the translated version    of text
   */
  TranslateText(event) {
    event.preventDefault();
    console.log(this.state.fromLang);
    console.log(this.state.toLang);
    console.log(event.target.value);
    // Ensures that languages have been selected before translating
    if (this.state.fromLang !== "none" && this.state.toLang !== "none") {
      const translated = getTranslation(
        event.target.value,
        this.state.fromLang,
        this.state.toLang
      );
      this.setState({ translation: translated, text: event.target.value });
    }
    this.setState({ text: event.target.value });
  }

  // Updates the selected language codes
  fromLangSelected(domEvent) {
    const selectedValue = domEvent.target[domEvent.target.selectedIndex].value;
    this.setState({ fromLang: selectedValue });
  }

  toLangSelected(domEvent) {
    const selectedValue = domEvent.target[domEvent.target.selectedIndex].value;
    this.setState({ toLang: selectedValue });
  }

  folderSelected(domEvent) {
    const selectedValue = domEvent.target[domEvent.target.selectedIndex].value;
    this.setState({ folder: selectedValue });
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
              <form
                id="myForm"
                action={`/usercards?userKey=${this.userKey}`}
                method="POST"
              >
                <li>
                  <span className="inline">
                    <label className="block">From:</label>
                    <LangaugeScroll
                      clickFunc={this.fromLangSelected}
                      selected={this.state.fromLang}
                      key="fromLang"
                    ></LangaugeScroll>
                  </span>
                  <span className="inline">
                    <label className="block">To:</label>
                    <LangaugeScroll
                      clickFunc={this.toLangSelected}
                      selected={this.state.toLang}
                      key="toLang"
                    ></LangaugeScroll>
                  </span>
                </li>
                <li>
                  <label className="block">Text:</label>
                  <input
                    id="mainText"
                    type="text"
                    placeholder={this.state.text}
                    onBlur={this.TranslateText}
                    required
                  ></input>
                </li>
                <li>
                  <label className="block">Translation:</label>
                  <input
                    id="translated"
                    type="text"
                    placeholder={this.state.translation}
                    readOnly
                  ></input>
                </li>
                <li>
                  <span className="inline">
                    <label className="block">Image:</label>
                    <input type="file" id="image" name="imageSelect"></input>
                  </span>
                  <span className="inline">
                    <label className="block">Folder:</label>
                    <FolderScroll
                      userKey={this.userKey}
                      clickFunc={this.folderSelected}
                      selected={this.state.folder}
                    ></FolderScroll>
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
