import React from "react";
import ReactDOM from "react-dom";

class AddPlace extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      description: "",
      x_coord: "",
      y_coord: "",
      image: ""
    };
  }

  componentDidMount() {
    this.fetchBlobstoreURL();
  }

  fetchBlobstoreURL() {
    fetch("/api/add_place_blobstore")
      .then(res => {
        return res.text();
      })
      .then(blobstoreURL => {
        this.setState({ blobstoreURL });
      });
  }

  render() {
    const {
      name,
      description,
      x_coord,
      y_coord,
      image,
      blobstoreURL
    } = this.state;

    if (blobstoreURL == null) {
      return "Loading...";
    }

    return (
      <div>
        <h1>Add Place</h1>
        <textarea
          name="name"
          placeholder={"Enter a location name"}
          value={this.state.name}
          onChange={e => {
            this.setState({ name: e.target.value });
          }}
        />
        <br />
        <textarea
          name="description"
          placeholder={"Enter your location's description"}
          value={this.state.description}
          onChange={e => {
            this.setState({ description: e.target.value });
          }}
        />
        <br />
        <textarea
          name="x_coord"
          placeholder={"Enter a X-coord"}
          value={this.state.x_coord}
          onChange={e => {
            this.setState({ x_coord: e.target.value });
          }}
        />
        <br />
        <textarea
          name="y_coord"
          placeholder={"Enter a Y-coord"}
          value={this.state.y_coord}
          onChange={e => {
            this.setState({ y_coord: e.target.value });
          }}
        />
        <br />
        Select image to upload:
        <input
          type="file"
          name="image"
          onChange={e => {
            this.setState({ image: e.target.files[0] });
          }}
        />
        <br />
        <button
          onClick={() => {
            const formData = new FormData();
            formData.append("name", this.state.name);
            formData.append("description", this.state.description);
            formData.append("x_coord", this.state.x_coord);
            formData.append("y_coord", this.state.y_coord);
            formData.append("image", this.state.image);
            fetch(blobstoreURL, {
              method: "POST",
              credentials: "same-origin",
              body: formData
            }).then(() => window.location.reload());
          }}
        >
          Submit
        </button>
      </div>
    );
  }
}

// Fetch data and populate the UI of the page.
const root = document.getElementById("root");
ReactDOM.render(<AddPlace />, root);
