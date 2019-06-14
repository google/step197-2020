
src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB8qFRVjUKlhJERpMDx9RIzjqiOsl4tIMM"

function createMap(){
    const map = new google.maps.Map(document.getElementById('map'), {
      center: {lat: 37.422, lng: -122.084},
      zoom: 16
    });
  }