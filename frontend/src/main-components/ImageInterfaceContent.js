import React from 'react';
import styled from '@emotion/styled';
import './ImageInterfaceContent.css';
import Cropper from 'react-easy-crop';
import getCroppedImg from '../sub-components/CropImage';
import LabelScroll from '../sub-components/labelScroll';

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
			labels: {},
			selectedLabel: 'Please select a label',
			imageUploadUrl: '',
			uploadUrlFetched: false,
		};
		this.imageSelected = this.imageSelected.bind(this);
		this.handleImageUpload = this.handleImageUpload.bind(this);
		this.handleSelectedLabel = this.handleSelectedLabel.bind(this);
		this.handleImageCrop = this.handleImageCrop.bind(this);
		this.onCropChange = this.onCropChange.bind(this);
		this.handleZoom = this.handleZoom.bind(this);
		this.handleReset = this.handleReset.bind(this);
		this.setCroppedAreaPixels = this.setCroppedAreaPixels.bind(this);
	}

	/**
	 * Creates a URL for the image that the User uploads
	 */
	imageSelected(event) {
		this.setState({ imageURL: URL.createObjectURL(event.target.files[0]) });
		console.log('Image Selected!');
	}
	handleSelectedLabel(domEvent) {
		const selectedValue = domEvent.target[domEvent.target.selectedIndex].value;
		this.setState({ selectedLabel: selectedValue });
	}

	handleImageUpload(event) {
		this.setState({ imageUploaded: true });
		console.log('Image Uploaded!');
	}

	handleImageCrop(event) {
		const getImage = async () => {
			const croppedImage = await getCroppedImg(
				this.state.imageURL,
				this.state.croppedAreaPixels,
				this.state.rotation
			);
			this.setState({ imageCropped: true, croppedImageURL: croppedImage });
			/* Insert fetching stuff */
		};
		getImage();
	}

	onCropChange(crop) {
		this.setState({ crop });
	}

	handleZoom(zoom) {
		this.setState({ zoom });
		console.log(zoom);
	}

	handleReset() {
		this.setState({ zoom: 1, crop: { x: 0, y: 0 } });
	}

	setCroppedAreaPixels(croppedArea, croppedAreaPixels) {
		this.setState({ croppedAreaPixels: croppedAreaPixels });
		console.log(croppedArea);
	}

	async componentDidMount() {
		try {
			const uploadUrl = await fetch('/upload').then((response) => response.text());
			if (!uploadUrl.ok) {
				throw Error(uploadUrl.statusText);
			}
			this.setState({ imageUploadUrl: uploadUrl, uploadUrlFetched: true });
		} catch (error) {
			alert('Please refresh page to upload image');
		}
	}

	render() {
		return (
			<div className="Container">
				<div className="ContentContainer">
					{this.state.imageCropped ? (
						<div className="FormContainer">
							<p>Please select a label that best describes the image:</p>
							<form>
								<LabelScroll
									clickFunc={this.handleSelectedLabel}
									selected={this.state.selectedLabel}
									key="selectedLabel"
								></LabelScroll>
							</form>
							<button className="Button" onClick={this.handleImageUpload}>
								Create flashcard with this label
							</button>
						</div>
					) : (
						<div className="FormContainer">
							<form id="imageForm">
								<label for="img">Please Upload an Image for Analysis:</label>
								<br />
								<input onChange={this.imageSelected} type="file" id="img" name="img" accept="image/*" />
							</form>
							<br />
							{this.state.imageURL ? (
								<button className="Button" onClick={this.handleImageUpload}>
									Upload this Image
								</button>
							) : null}
						</div>
					)}
					{this.state.imageUploaded ? (
						this.state.imageCropped ? (
							<div className="ImageContainer">
								<img className="CroppedImage" src={this.state.croppedImageURL} />
							</div>
						) : (
							<div className="ImageContainer">
								<div className="CropContainer">
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
								<div className="ControlsContainer">
									<p>Controls: Zoom = Scoll, Pan = MouseDrag</p>
									<div className="ButtonContainer">
										<button className="Button" onClick={this.handleReset}>
											Reset to Default
										</button>
										<form method="POST" enctype="multipart/form-data" action={this.state.imageUploadUrl}>
											<input
												className="Button"
												type="submit"
												onClick={this.handleImageCrop}
												value="Crop"
											></input>
										</form>
									</div>
								</div>
							</div>
						)
					) : (
						<div className="NoImageContainer">
							Please Upload an Image to use the Image Analysis feature.
						</div>
					)}
				</div>
			</div>
		);
	}
}

export default ImageInterfaceContent;
