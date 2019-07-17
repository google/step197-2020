import React from "react";
import ReactDOM from "react-dom";

class AddPlace extends React.Component {
  constructor(props) {
    super(props);
    this.state = { name: "", x_coord: "", y_coord: "", image: "" };
  }

  render() {
    const { name, x_coord, y_coord, image } = this.state;
    const placeObject = { name, x_coord, y_coord, image };

    return (
      <div>
        <h1>Add Place</h1>
        <textarea
          placeholder={"Enter a name"}
          value={this.state.name}
          onChange={e => {
            this.setState({ name: e.target.value });
          }}
        ></textarea>
        <br />
        <textarea
          placeholder={"Enter a X-coord"}
          value={this.state.x_coord}
          onChange={e => {
            this.setState({ x_coord: e.target.value });
          }}
        ></textarea>
        <br />
        <textarea
          placeholder={"Enter a Y-coord"}
          value={this.state.y_coord}
          onChange={e => {
            this.setState({ y_coord: e.target.value });
          }}
        ></textarea>
        <br />
        Select image to upload:
        <input
          type="file"
          name="fileToUpload"
          onChange={e => {
            this.setState({ image: e.target.files[0] });
          }}
        />
        <br />
        <button
          onClick={() => {
            const formData = new FormData();
            formData.append("name", this.state.name);
            formData.append("x_coord", this.state.x_coord);
            formData.append("y_coord", this.state.y_coord);
            formData.append("image", this.state.image);
            fetch("/api/place", {
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