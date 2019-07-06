import React from "react";
import ReactDOM from "react-dom";

function createMap() {
  fetch("/charging-station-data")
    .then(function(response) {
      return response.json();
    })
    .then(chargingStations => {
      const map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 37.422, lng: -122.084 },
        zoom: 7
      });

      chargingStations.forEach(chargingStation => {
        addLandmark(
          map,
          chargingStation.lat,
          chargingStation.lng,
          "EV Charging Station",
          "Electric Charging Station Located Here!"
        );
      });
    });
}

/** Adds a marker that shows an info window when clicked. */
function addLandmark(map, lat, lng, title, description) {
  const marker = new google.maps.Marker({
    position: { lat: lat, lng: lng },
    map: map,
    title: title
  });
  const infoWindow = new google.maps.InfoWindow({
    content: description
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

const rows = [
  createData(
    12345,
    "User1",
    "Oregon State University",
    "My university",
    44.563782,
    -123.279442,
    "2019-06-01 08:15:00"
  ),
  createData(
    23456,
    "User2",
    "Penelope’s Coffee",
    "Favorite coffee shop",
    37.56188,
    -122.26644,
    "2019-06-15 23:01:00"
  ),
  createData(
    34567,
    "User3",
    "Victoria Falls",
    "Top of Bucket List Location",
    -17.9289,
    25.83671,
    "2017-04-15 01:25:00"
  ),
  createData(
    45678,
    "User4",
    "Multnoma Falls",
    "Favorite hiking spot",
    45.5394,
    -122.21716,
    "2019-02-05 12:00:00"
  ),
  createData(
    56789,
    "User5",
    "Mary’s Peak",
    "Favorite Sunrise Spot",
    44.47477,
    -123.52512,
    "2018-11-25 20:59:00"
  ),
  createData(
    67890,
    "User6",
    "Mt. Haleakala",
    "Best over-the-clouds sunrise spot",
    20.77355,
    -156.2432,
    "2010-05-05 03:45:00"
  )
];

function PinList(props) {
  const content = (
    <ul>
      {props.rows.map(pin => (
        <li>
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
  createMap();
  const rootElement = document.getElementById("PinList");
  ReactDOM.render(<PinList rows={rows} />, rootElement);
}
