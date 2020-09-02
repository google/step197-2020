import React, { Component } from "react";
import css from "./CreateCardContent.css";
import FrontCard from "../flashcards/FlashcardFrontPreview";
import BackCard from "../flashcards/FlashcardBackPreview";
import { getTranslation } from "../sub-components/translate";
import LangaugeScroll from "../sub-components/languageScroll";
import FolderScroll from "../sub-components/folderScroll";
import PageLoading from "../sub-components/PageLoading";
import TranslationLoading from "../sub-components/TranslationLoading";

class CreateCardContent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      imgSrc: "",
      text: "",
      translation: "none",
      fromLang: "none",
      toLang: "none",
      folder: "",
      uploadUrlFetched: false,
      imageUploadUrl: "",
      loading: false,
    };
    this.translateText = this.translateText.bind(this);
    this.fromLangSelected = this.fromLangSelected.bind(this);
    this.toLangSelected = this.toLangSelected.bind(this);
    this.folderSelected = this.folderSelected.bind(this);
    this.imageSelected = this.imageSelected.bind(this);
    this.textChanged = this.textChanged.bind(this);
  }

  async componentDidMount() {
    const text = this.props.word;
    try {
      const uploadResponse = await fetch("/upload");
      const uploadUrl = await uploadResponse.text();
      if (!uploadResponse.ok) {
        throw Error(uploadUrl.statusText);
      }
      this.setState({
        imageUploadUrl: uploadUrl,
        uploadUrlFetched: true,
        text,
      });
    } catch (error) {
      alert("Refresh page to create a card");
    }
  }

  /**
   * When the user has finished typing, the Google Translate
   * API is called to fetch the translated version of the text input.
   */
  translateText(event) {
    const text = event.target.value;
    this.setState({ loading: true });
    // Ensures that languages have been selected before translating
    if (this.state.fromLang !== "none" && this.state.toLang !== "none") {
      (async () => {
        const toLang = this.state.toLang;
        // If an error is thrown it is caught inside of get translation
        const translated = await getTranslation(
          text,
          this.state.fromLang,
          this.state.toLang
        );
        this.setState((prevState) => {
          // Checks if fields have been changed since the last request to getTranslation
          if (prevState.toLang != toLang || prevState.text != text) {
            return;
          }
          return { translation: translated.translation, loading: false, text};
        });
      })();
    }
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

  folderSelected(domEvent) {
    const selectedValue = domEvent.target[domEvent.target.selectedIndex].value;
    this.setState({ folder: selectedValue });
  }

  imageSelected(event) {
    this.setState({ imgSrc: URL.createObjectURL(event.target.files[0]) });
  }

  textChanged(event) {
    this.setState({ text: event.target.value });
  }

  render() {
    if (!this.state.uploadUrlFetched) {
      return (
        <div className='loadingContainer'>
          <PageLoading></PageLoading>
        </div>
      );
    }
    return (
      <div className='container'>
        <div id='innerContainer'>
          <div id='cardPreview'>
            <div className='holdCard'>
              <FrontCard
                image={this.state.imgSrc}
                text={this.state.text}></FrontCard>
            </div>
            <div className='holdCard'>
              <BackCard text={this.state.translation}></BackCard>
            </div>
          </div>
          <div id='formBox'>
            <ul>
              <form
                id='myForm'
                action={this.state.imageUploadUrl}
                method='POST'
                encType='multipart/form-data'>
                <li>
                  <span className='inline'>
                    <label className='block'>From:</label>
                    <LangaugeScroll
                      clickFunc={this.fromLangSelected}
                      selected={this.state.fromLang}
                      key='fromLang'></LangaugeScroll>
                  </span>
                  <span className='inline'>
                    <label className='block'>To:</label>
                    <LangaugeScroll
                      clickFunc={this.toLangSelected}
                      selected={this.state.toLang}
                      key='toLang'></LangaugeScroll>
                  </span>
                </li>
                <li>
                  <label className='block'>Text:</label>
                  <input
                    id='mainText'
                    type='text'
                    name='rawText'
                    value={this.state.text}
                    onBlur={this.translateText}
                    onChange={this.textChanged}
                    required></input>
                </li>
                <li>
                  <label className='block'>Translation:</label>
                    <input
                      id='translated'
                      type='text'
                      name='translatedText'
                      value={this.state.translation}
                      readOnly></input>
                    <TranslationLoading loading={this.state.loading}></TranslationLoading>
                </li>
                <li>
                  <span className='inline'>
                    <label className='block'>Image:</label>
                    <input
                      type='file'
                      id='image'
                      name='image'
                      onChange={this.imageSelected}></input>
                  </span>
                  <span className='inline'>
                    <label className='block'>Folder:</label>
                    <FolderScroll
                      userKey={this.userKey}
                      clickFunc={this.folderSelected}
                      selected={this.state.folder}></FolderScroll>
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