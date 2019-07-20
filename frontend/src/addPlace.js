import React from "react";
import ReactDOM from "react-dom";
import { createMap } from "./home.js";
import Navigation from "./Navigation";

class SearchBar extends React.Component {
  constructor(props) {
    super(props);
    this.searchbar = React.createRef();
    this.map = React.createRef();
    this.setupSearchBar = this.setupSearchBar.bind(this);
  }

  componentDidMount() {
    this.setupSearchBar();
  }

  setupSearchBar() {
    let map = createMap([], this.map.current);
    let searchBox = new google.maps.places.Autocomplete(this.searchbar.current);

    // Bias the SearchBox results towards current map's viewport.
    map.addListener("bounds_changed", () => {
      searchBox.setBounds(map.getBounds());
    });

    var marker = null;
    // Listen for the event fired when the user selects a prediction and retrieve
    // more details for that place.
    searchBox.addListener("place_changed", () => {
      var place = searchBox.getPlace();
      const { location } = place.geometry;

      this.props.onChange({
        lat: location.lat(),
        lng: location.lng()
      });

      // Clear out the old markers.
      if (marker != null) {
        marker.setMap(null);
      }
      marker = null;

      // For each place, get the icon, name and location.
      var bounds = new google.maps.LatLngBounds();
      if (!place.geometry) {
        console.log("Returned place contains no geometry");
        return;
      }
      var icon = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };
      // Create a marker for each place.
      marker = new google.maps.Marker({
        map: map,
        icon: icon,
        title: place.name,
        position: place.geometry.location
      });

      if (place.geometry.viewport) {
        // Only geocodes have viewport.
        bounds.union(place.geometry.viewport);
      } else {
        bounds.extend(place.geometry.location);
      }
      map.fitBounds(bounds);
    });
  }
  render() {
    const { value, onChange } = this.props;
    return (
      <div>
        <label htmlFor="searchbar">Search for a place: </label>
        <input id="searchbar" ref={this.searchbar} style={{ width: "500px" }} />
        <div id="map" ref={this.map} />
      </div>
    );
  }
}

class AddPlace extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      description: "",
      latlng: {},
      image: null
    };
    this.fetchBlobstoreURL = this.fetchBlobstoreURL.bind(this);
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
    const { name, description, image, blobstoreURL } = this.state;

    if (blobstoreURL == null) {
      return (
        <div>
          <Navigation />
          Loading...
        </div>
      );
    }

    return (
      <div>
        <Navigation />
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
        <SearchBar
          value={this.state.latlng}
          onChange={latlng => this.setState({ latlng })}
        />
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
            formData.append("lng", this.state.latlng.lng);
            formData.append("lat", this.state.latlng.lat);
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
