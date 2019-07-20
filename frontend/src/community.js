import React from "react";
import ReactDOM from "react-dom";
import Navigation from "./Navigation";

function User({ user }) {
  return (
    <React.Fragment>
      <a href={"/user-page?user=" + user.email}>
        <h3>{user.email}</h3>
      </a>
      <h4>About</h4>
      <div>{user.aboutMe}</div>
      <hr />
    </React.Fragment>
  );
}

function CommunityPage() {
  const [users, setUsers] = React.useState();
  React.useEffect(() => {
    fetch("/api/users")
      .then(res => {
        return res.json();
      })
      .then(users => {
        setUsers(users);
      });
  }, []);

  let inner;
  if (users === undefined) {
    inner = "Loading...";
  } else {
    inner = users.map(user => <User key={user.email} user={user} />);
  }

  return (
    <div>
      <Navigation />
      <h1>Community</h1>
      {inner}
    </div>
  );
}

const root = document.getElementById("root");
ReactDOM.render(<CommunityPage />, root);

addLoginOrLogoutLinkToNavigation();
