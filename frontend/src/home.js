import React from "react";
import ReactDOM from "react-dom";

const rows = [
  createData(
    23422345,
    "ThomasWeathers",
    "Oregon State University",
    "My university! The campus is very pretty, built mostly of brick and new modern metal-framed buildings. Check out the lounge in the Memorial Union for a great study spot, or the coffee shop within Weatherford Hall.",
    44.563782,
    -123.279442,
    "2019-06-01 08:15:00"
  ),
  createData(
    23456,
    "ThomasWeathers",
    "Penelope’s Coffee",
    "Favorite coffee shop! Penelopes is a small family owned business who has many different types of coffee, with an emphasis on different greek coffees. My favorite coffee is their Ethiopian Jebena",
    37.56188,
    -122.26644,
    "2019-06-15 23:01:00"
  ),
  createData(
    34567,
    "ThomasWeathers",
    "Victoria Falls",
    "Top of Bucket List Location! This is the longest waterfall in the world, serving as the border between Zambia and Zimbabwe. ",
    -17.9289,
    25.83671,
    "2017-04-15 01:25:00"
  ),
  createData(
    45678,
    "ThomasWeathers",
    "Multnoma Falls",
    "My Favorite hiking spot! This waterfall is surrounded by many different hiking trails and smaller waterfalls in the surrounding Colombia River Gorge.",
    45.5394,
    -122.21716,
    "2019-02-05 12:00:00"
  ),
  createData(
    56789,
    "ThomasWeathers",
    "Mary’s Peak",
    "Favorite Sunrise Spot. This spot is a 30 minute drive from the base to the summit, with hiking trails, and many open lookouts to the surrounding wilamette valley",
    44.47477,
    -123.52512,
    "2018-11-25 20:59:00"
  ),
  createData(
    67890,
    "ThomasWeathers",
    "Mt. Haleakala",
    "Best over-the-clouds sunrise spot. This is a nostalgic spot for me, where I used to wake up early to drive up this volcano to watch the sunrise above the coulds.",
    20.77355,
    -156.2432,
    "2010-05-05 03:45:00"
  ),
  createData(
    345632,
    "JanetVu",
    "East Kentwood High School",
    "My old high school! Lots of interesting experiences from the diversity (socioeconomic, racial, etc. and rated #7 in the nation by Niche). Huge emphasis on sports",
    42.85011,
    -85.619687,
    "2019-07-07 20:16:35"
  ),
  createData(
    34563456,
    "SophiaQin",
    "Union Square",
    "A shopper's paradise filled with good food, an abundance of stores, and entertainment in SF.",
    37.787979,
    -122.407516,
    "2019-07-07 14:30:00"
  ),
  createData(
    2345523,
    "SophiaQin",
    "University of California, Berkeley",
    "A public university offering a variety of undergraduate and graduate programs with approximately 40,000 students enrolled.",
    37.870628,
    -122.279762,
    "2019-07-07 14:40:00"
  )
];
function createMap() {
  const map = new google.maps.Map(document.getElementById("map"), {
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
  contentDescription.innerText = description + lineBreak + owner + colonSpace + timestamp;

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
};
