import React from "react";
import ReactDOM from "react-dom";

class AddPlace extends React.Component {
  constructor(props) {
    super(props);
    this.state = { name: "", x_coord: "", y_coord: "" };
  }

  render() {
    const { name, x_coord, y_coord } = this.state;
    const placeObject = { name, x_coord, y_coord };

    return (
      <div>
        <h1>Add Place</h1>
        <textarea value={this.state.value} onChange={e => {this.setState({ name: e.target.value})}}>
        Enter a location name</textarea>
        <br />
        <textarea value={this.state.value} onChange={e => {this.setState({ x_coord: e.target.value })}}>Enter a X coord</textarea>
        <br />
        <textarea value={this.state.value} onChange={e => {this.setState({ y_coord: e.target.value })}}>Enter a Y coord</textarea>
        <br />
        <button
          onClick={() =>
            fetch("/api/place", {
              method: "POST",
              credentials: "same-origin",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(placeObject)
            }).then(() => window.location.reload())
          }
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
