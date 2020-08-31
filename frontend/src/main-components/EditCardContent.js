import React, { Component } from 'react';
import css from './CreateCardContent.css';
import FrontCard from '../flashcards/FlashcardFrontPreview';
import BackCard from '../flashcards/FlashcardBackPreview';
import { getTranslation } from '../sub-components/translate';
import LangaugeScroll from '../sub-components/languageScroll';

class EditCardContent extends Component {
	constructor(props) {
		super(props);
		this.state = {
			imgSrc: this.props.imageBlobKey,
			text: this.props.rawText,
			translation: this.props.textTranslated,
			fromLang: 'none',
			toLang: 'none',
			uploadUrlFetched: false,
			imageUploadUrl: '',
		};
		this.translateText = this.translateText.bind(this);
		this.fromLangSelected = this.fromLangSelected.bind(this);
		this.toLangSelected = this.toLangSelected.bind(this);
		this.imageSelected = this.imageSelected.bind(this);
		this.textChanged = this.textChanged.bind(this);
	}

	async componentDidMount() {
		const uploadResponse = await fetch('/editcardupload');
		const uploadUrl = await uploadResponse.text();
		console.log('Got the response!');
		if (!uploadResponse.ok) {
			console.log('The response is not ok!');
			throw Error(uploadUrl.statusText);
		}
		this.setState({ imageUploadUrl: uploadUrl, uploadUrlFetched: true });
	}

	/**
	 * When the user has finished typing, the Google Translate
	 * API is called to fetch the translated version of the text input.
	 */
	translateText(event) {
		const text = event.target.value;
		// Ensures that languages have been selected before translating
		if (this.state.fromLang !== 'none' && this.state.toLang !== 'none') {
			(async () => {
				const toLang = this.state.toLang;
				// If an error is thrown it is caught inside of get translation
				const translated = await getTranslation(text, this.state.fromLang, this.state.toLang);
				this.setState((prevState) => {
					// Checks if fields have been changed since the request to getTranslation
					if (prevState.toLang != toLang || prevState.text != text) {
						return;
					}
					return { translation: translated.translation, text };
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

	imageSelected(event) {
		this.setState({ imgSrc: URL.createObjectURL(event.target.files[0]) });
	}

	textChanged(event) {
		this.setState({ text: event.target.value });
	}

	render() {
		if (!this.state.uploadUrlFetched) {
			return <h1>Loading</h1>;
		}
		return (
			<div id="container">
				<div id="innerContainer">
					<div id="CardPreview">
						<div className="holdCard">
							<FrontCard image={this.state.imgSrc} text={this.state.text}></FrontCard>
						</div>
						<div className="holdCard">
							<BackCard text={this.state.translation}></BackCard>
						</div>
					</div>
					<div id="formBox">
						<ul>
							<form
								id="myForm"
								action={this.state.imageUploadUrl}
								method="POST"
								encType="multipart/form-data"
							>
								<input type="hidden" name="_METHOD" value="PUT" />

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
										name="rawText"
										placeholder={this.state.text}
										onBlur={this.translateText}
										onChange={this.textChanged}
										required
									></input>
								</li>
								<li>
									<label className="block">Translation:</label>
									<input
										id="translated"
										type="text"
										name="translatedText"
										value={this.state.translation}
										readOnly
									></input>
								</li>
								<li>
									<span className="inline">
										<label className="block">Image:</label>
										<input
											type="file"
											id="image"
											name="image"
											onChange={this.imageSelected}
										></input>
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

export default EditCardContent;
