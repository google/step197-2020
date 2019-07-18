import React from "react";
import ReactDOM from "react-dom";

function User({ user }) {
  return (
    <React.Fragment>
      <h2>{user.email}</h2>
      <div>{user.aboutMe}</div>
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
      <h1>Community</h1>
      {inner}
    </div>
  );
}

const root = document.getElementById('root');
ReactDOM.render(<CommunityPage />, root);
