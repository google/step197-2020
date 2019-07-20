import React from "react";
import ReactDOM from "react-dom";

export function createMap(rows, element = document.getElementById("map")) {
  const map = new google.maps.Map(element, {
    center: { lat: 37.422, lng: -122.084 },
    zoom: 7
  });
  rows.forEach(pin => {
    addLandmark(
      map,
      pin.latitude,
      pin.longitude,
      pin.title,
      pin.description,
      pin.owner,
      pin.timestamp
    );
  });
  return map;
}

/** Adds a marker that shows an info window when clicked. */
function addLandmark(map, lat, lng, title, description, owner, timestamp) {
  const marker = new google.maps.Marker({
    position: { lat: lat, lng: lng },
    map: map,
    title: title
  });

  /** Creates DOM nodes to append to root div inside Google Maps info window */
  const contentRoot = document.createElement("div");
  const contentHeader = document.createElement("h1");
  const contentDescription = document.createElement("p");
  const lineBreak = "\n";
  const colonSpace = ": ";

  contentHeader.innerText = title;
  contentDescription.innerText =
    description + lineBreak + owner + colonSpace + timestamp;

  contentRoot.appendChild(contentHeader);
  contentRoot.appendChild(contentDescription);

  const infoWindow = new google.maps.InfoWindow({
    content: contentRoot
  });
  marker.addListener("click", function() {
    infoWindow.open(map, marker);
  });
}

function createData(
  id,
  owner,
  title,
  description,
  latitude,
  longitude,
  timestamp
) {
  return { id, owner, title, description, latitude, longitude, timestamp };
}

export function PinList(props) {
  const content = (
    <ul>
      {props.rows.map((pin, i) => (
        <li key={i}>
          {pin.owner}-- {pin.title}: "{pin.description}" ( {pin.latitude},
          {pin.longitude})
        </li>
      ))}
    </ul>
  );
  return (
    <div className="listContainer">
      <h1>Pins Created</h1>
      {content}
    </div>
  );
}

window["initialize"] = function initialize() {
  fetch("/api/place")
    .then(response => {
      return response.json();
    })
    .then(rows => {
      createMap(rows);
      const rootElement = document.getElementById("PinList");
      ReactDOM.render(<PinList rows={rows} />, rootElement);
    });
};
