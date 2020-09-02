import React, { Component } from "react";
import css from './CardForm.css';
import { getTranslation } from '../sub-components/translate';
import LangaugeScroll from '../sub-components/languageScroll';
import FolderScroll from '../sub-components/folderScroll';
import LabelScroll from '../sub-components/labelScroll';

class CardForm extends Component {
	constructor(props) {
		super(props);
		this.state = {
			imgSrc: '',
			text: '',
			translation: 'none',
			fromLang: 'none',
			toLang: 'none',
			folder: '',
			imageUploadUrl: this.props.submitUrl,
			selectedLabel: "",
		};
		this.translateText = this.translateText.bind(this);
		this.fromLangSelected = this.fromLangSelected.bind(this);
		this.toLangSelected = this.toLangSelected.bind(this);
		this.folderSelected = this.folderSelected.bind(this);
		this.imageSelected = this.imageSelected.bind(this);
		this.textChanged = this.textChanged.bind(this);
		this.postForm = this.postForm.bind(this);
	}
	async postForm(){
		let formData = new FormData();
		formData.append("image", this.props.image);
		formData.append("translatedText", this.state.translation);
		formData.append("rawText", this.state.text);
		formData.append("folderKey", this.state.folder);
		await fetch(this.props.uploadUrl, { method: 'POST', body: formData })
	}

	/**
	 * When the user has selected a label, the Google Translate
	 * API is called to fetch the translated version of the text input.
	 */
	translateText(event) {
		const text = domEvent.target[domEvent.target.selectedIndex].value;
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
					return { translation: translated.translation, text: text};
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
		const labelItems = this.props.labels.map((label) => {
			return (
				<option key={label} value={label}>
					{label}
				</option>
			);
		});
		return (
			<div id="formBox">
				<ul>
					<form id="myForm" onsubmit={this.postForm}>
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
							<label className="block">Text Label:</label>
							<LabelScroll
							            id = "mainText"
										labels={this.props.labels}
										clickFunc={this.translateText}
										selected={this.state.selectedLabel}
										name="rawText"
										required
							></LabelScroll>
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
		);
	}
}

export default CardForm;
