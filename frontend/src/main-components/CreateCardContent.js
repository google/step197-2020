import React, { Component } from "react";
import css from "./CreateCardContent.css";
import FrontCard from "../flashcards/FlashcardFrontPreview";
import BackCard from "../flashcards/FlashcardBackPreview";
import { getTranslation } from "../sub-components/translate";
import LangaugeScroll from "../sub-components/languageScroll";
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
    this.translateText = this.translateText.bind(this);
    this.fromLangSelected = this.fromLangSelected.bind(this);
    this.toLangSelected = this.toLangSelected.bind(this);
  }

  /**
   * When the user has finished typing, the Google Translate
   * API is called to fetch the translated version of the text input.
   */
  translateText(event) {
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

  // Updates the selected language code for future calls to the translateText function
  fromLangSelected(domEvent) {
    const selectedValue = domEvent.target[domEvent.target.selectedIndex].value;
    this.setState({ fromLang: selectedValue });
  }

  toLangSelected(domEvent) {
    const selectedValue = domEvent.target[domEvent.target.selectedIndex].value;
    this.setState({ toLang: selectedValue });
  }

  render() {
    return (
      <div id='container'>
        <div id='innerContainer'>
          <div id='CardPreview'>
            <div className='holdCard'>
              <FrontCard image={this.state.imgSrc}
                text={this.state.text}>
              </FrontCard>
            </div>
            <div className='holdCard'>
              <BackCard text={this.state.translation}></BackCard>
            </div>
          </div>

          <div id='formBox'>
            <ul>
              <form id='myForm' action='/usercards'>
                <li>
                  <span className='inline'>
                    <label className='block'>From:</label>
                    <LangaugeScroll
                      clickFunc={this.fromLangSelected}
                      selected={this.state.fromLang}
                      key='from'></LangaugeScroll>
                  </span>
                  <span className='inline'>
                    <label className='block'>To:</label>
                    <LangaugeScroll
                      clickFunc={this.toLangSelected}
                      selected={this.state.toLang}
                      key='to'></LangaugeScroll>
                  </span>
                </li>
                <li>
                  <label className='block'>Text:</label>
                  <input
                    id='mainText'
                    type='text'
                    placeholder={this.state.text}
                    onBlur={this.translateText}
                    required></input>
                </li>
                <li>
                  <label className='block'>Translation:</label>
                  <input
                    id='translate'
                    type='text'
                    placeholder={this.state.translation}
                    readOnly></input>
                </li>
                <li>
                  <span className='inline'>
                    <label className='block'>Image:</label>
                    <input type='file' id='image' name='imageSelect'></input>
                  </span>
                  <span className='inline'>
                    <label className='block'>Folder:</label>
                    <input type='file' id='folder' name='folderSelect'></input>
                  </span>
                </li>

                <li>
                  <input id='submission' type='submit' value='Submit' />
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
