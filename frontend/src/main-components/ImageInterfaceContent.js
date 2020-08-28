import React from "react";
import styled from "@emotion/styled";
import "./ImageInterfaceContent.css";
import Cropper from "react-easy-crop";
import getCroppedImg from "../sub-components/CropImage";
import LabelScroll from "../sub-components/labelScroll";
import { Link } from "react-router-dom";

const CONTAINER_HEIGHT = 500;
class ImageInterfaceContent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      imageUploaded: false,
      imageCropped: false,
      imageURL: "",
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
      croppedImageURL: "",
      labels: [],
      selectedLabel: "Please select a label",
      imageUploadUrl: "",
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
    this.fetchLabels = this.fetchLabels.bind(this);
  }

  imageSelected(event) {
    this.setState({ imageURL: URL.createObjectURL(event.target.files[0]) });
  }
  handleSelectedLabel(domEvent) {
    const selectedValue = domEvent.target[domEvent.target.selectedIndex].value;
    this.setState({ selectedLabel: selectedValue });
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
    try {
      const uploadUrl = await fetch("/upload").then((response) =>
        response.text()
      );
      if (!uploadUrl.ok) {
        throw Error(uploadUrl.statusText);
      }
      this.setState({ imageUploadUrl: uploadUrl, uploadUrlFetched: true });
    } catch (error) {
      alert("Please refresh page to upload image");
    }
  }

  async postData(url = "", image = "") {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(image),
    });
    return response.text();
  }

  fetchLabels() {
    postData("/ObjectDectectionUpload", this.state.croppedImageURL)
      .then((labels) => {
        this.setState({ labels: labels });
      })
      .catch((error) => {
        alert("Could not fetch labels, please try refreshing the page.");
      });
  }

  render() {
    return (
      <div className="container">
        <div className="contentContainer">
          {this.state.imageCropped ? (
            <div className="formContainer">
              <p>Please select a label that best describes the image:</p>
              <form>
                <LabelScroll
                  labels={this.state.labels}
                  clickFunc={this.handleSelectedLabel}
                  selected={this.state.selectedLabel}
                  name="selectedLabel"
                ></LabelScroll>
              </form>
              <Link
                to={`/CreateCard?image=${this.state.croppedImageURL}?text=${this.state.selectedLabel}`}
              >
                <button className="button">
                  Create flashcard with this label
                </button>
              </Link>
            </div>
          ) : (
            <div className="formContainer">
              <form id="imageForm">
                <label for="img">Please Upload an Image for Analysis:</label>
                <br />
                <input
                  onChange={this.imageSelected}
                  type="file"
                  id="img"
                  name="img"
                  accept="image/*"
                />
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
                <img
                  className="croppedImage"
                  src={this.state.croppedImageURL}
                />
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
