import React from 'react';
import styled from '@emotion/styled';
import './ImageInterfaceContent.css';
import Cropper from 'react-easy-crop';
import getCroppedImg from '../sub-components/CropImage';
import LabelScroll from '../sub-components/labelScroll';
import CardForm from '../sub-components/CardForm';
import { Link } from 'react-router-dom';

const CONTAINER_HEIGHT = 500;
class ImageInterfaceContent extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			imageUploaded: false,
			imageCropped: false,
			imageURL: '',
			crop: { x: 0, y: 0 },
			zoom: 1,
			aspect: 4 / 3,
			rotation: 0,
			croppedAreaPixels: {
				x: 0,
				y: 0,
				width: 0,
				height: 0,
			},
			croppedImageURL: '',
			labels: [],
			selectedLabel: 'Please select a label',
			imageUploadUrl: '',
			uploadUrlFetched: false,
			labelsFetched: false,
			submitURL: '',
		};
		this.imageSelected = this.imageSelected.bind(this);
		this.handleImageUpload = this.handleImageUpload.bind(this);
		this.handleImageCrop = this.handleImageCrop.bind(this);
		this.onCropChange = this.onCropChange.bind(this);
		this.handleZoom = this.handleZoom.bind(this);
		this.handleReset = this.handleReset.bind(this);
		this.setCroppedAreaPixels = this.setCroppedAreaPixels.bind(this);
		this.fetchLabels = this.fetchLabels.bind(this);
	}

	imageSelected(event) {
		this.setState({ imageURL: URL.createObjectURL(event.target.files[0]) });
	}

	handleImageUpload(event) {
		this.setState({ imageUploaded: true });
	}

	handleImageCrop(event) {
		const getImage = async () => {
			const croppedImage = await getCroppedImg(
				this.state.imageURL,
				this.state.croppedAreaPixels,
				this.state.rotation
			);
			this.setState({ imageCropped: true, croppedImageURL: croppedImage });
			this.fetchLabels();
		};
		getImage();
	}

	onCropChange(crop) {
		this.setState({ crop });
	}

	handleZoom(zoom) {
		this.setState({ zoom });
	}

	handleReset() {
		this.setState({ zoom: 1, crop: { x: 0, y: 0 } });
	}

	setCroppedAreaPixels(croppedArea, croppedAreaPixels) {
		this.setState({ croppedAreaPixels: croppedAreaPixels });
	}

	async componentDidMount() {
		const uploadResponse = await fetch('/ObjectDetectionUpload');
		const uploadUrl = await uploadResponse.text();
		if (!uploadResponse.ok) {
			throw Error(uploadUrl.statusText);
		}
		const submitResponse = await fetch('/upload');
		const submitUrl = await submitResponse.text();
		if (!submitResponse.ok) {
			throw Error(submitUrl.statusText);
		}
		console.log('Upload URLS:');
		console.log(uploadUrl);
		console.log(submitUrl);
		this.setState({ imageUploadUrl: uploadUrl, uploadUrlFetched: true, submitURL: submitURL });
		/*try {
			console.log('Trying to upload url...');
			const uploadUrl = await fetch('/ObjectDetectionUpload').then((response) => response.text());
			console.log('This is the upload URL:');
			console.log(uploadUrl);
			if (!uploadUrl.ok) {
				throw Error(uploadUrl.statusText);
			}
			this.setState({ imageUploadUrl: uploadUrl, uploadUrlFetched: true });
		} catch (error) {
			console.log('UPLOADING URL DID NOT WORK!!!');
			alert('Please refresh page to upload image');
		}
		*/
	}

	/*async postData(url = '', data = {}) {
		console.log('Starting Post Data');
		const response = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(data),
		});
		console.log(response.text());
		return response.text();
	}*/

	async fetchLabels() {
		let formData = new FormData();
		formData.append('image', this.state.croppedImageURL);

		console.log("It's fetching Time!");
		await fetch(this.state.imageUploadUrl, { method: 'POST', body: formData })
			.then((response) => response.text)
			.then((labels) => {
				this.setState({ labels: labels });
			})
			.catch((error) => {
				console.log(error);
				console.log('Fetching Labels did not work!!!');
			});

		/*this.postData(this.state.imageUploadUrl, { image: this.state.croppedImageURL })
			.then((labels) => {
				console.log(labels);
				this.setState({ labels: labels });
			})
			.catch((error) => {
				console.log('Fetching Labels did not work!!!');
			});*/
	}

	render() {
		return (
			<div className="container">
				<div className="contentContainer">
					{this.state.imageCropped ? (
						<div className="formContainer">
							<p>Please select a label that best describes the image:</p>
							{this.state.labelsFetched ? (
								<CardForm
									labels={this.state.labels}
									image={this.state.croppedImageURL}
									uploadUrl={this.state.submitURL}
								></CardForm>
							) : (
								<p>Loading Labels....</p>
							)}
						</div>
					) : (
						<div className="formContainer">
							<form id="imageForm">
								<label>
									Please Upload an Image for Analysis:
									<br />
									<input
										onChange={this.imageSelected}
										type="file"
										id="img"
										name="img"
										accept="image/*"
									/>
								</label>
							</form>
							<br />
							{this.state.imageURL ? (
								<button className="button" onClick={this.handleImageUpload}>
									Upload this Image
								</button>
							) : null}
						</div>
					)}
					{this.state.imageUploaded ? (
						this.state.imageCropped ? (
							<div className="imageContainer">
								<img className="croppedImage" src={this.state.croppedImageURL} />
							</div>
						) : (
							<div className="imageContainer">
								<div className="cropContainer">
									<Cropper
										image={this.state.imageURL}
										crop={this.state.crop}
										zoom={this.state.zoom}
										aspect={this.state.aspect}
										onCropChange={this.onCropChange}
										onCropComplete={this.setCroppedAreaPixels}
										onZoomChange={this.handleZoom}
										onMediaLoaded={(mediaSize) => {
											onZoomChange(CONTAINER_HEIGHT / mediaSize.naturalHeight);
										}}
									/>
								</div>
								<br />
								<div className="controlsContainer">
									<p>Controls: Zoom = Scoll, Pan = MouseDrag</p>
									<div className="buttonContainer">
										<button className="button" onClick={this.handleReset}>
											Reset to Default
										</button>
										<button className="button" onClick={this.handleImageCrop}>
											Crop
										</button>
									</div>
								</div>
							</div>
						)
					) : (
						<div className="noImageContainer">
							Please Upload an Image to use the Image Analysis feature.
						</div>
					)}
				</div>
			</div>
		);
	}
}

export default ImageInterfaceContent;
