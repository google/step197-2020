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
