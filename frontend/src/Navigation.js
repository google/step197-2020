/**
 * Creates navigation depending on whether user is logged in or not
 */

import React, { useState, useEffect, Fragment } from "react";

function Navigation() {
  const [username, setUsername] = useState(null);

  useEffect(() => {
    fetch("/login-status")
      .then(response => response.json())
      .then(loginStatus => {
        setUsername(loginStatus.username);
      })
      .catch(e => console.log(e));
  }, []);

  return (
    <nav
      id="navigation"
      style={{
        display: "inline",
        backgroundColor: "rgba(255, 255, 255, 0.8)",
        padding: 5,
        top: 0,
        marginBottom: 10,
        position: "sticky"
      }}
    >
      <NavLink url="/" text="8lobal" style={{ float: "left" }} />
      <NavLink url="/" text="Home" />
      <NavLink url="/aboutus.html" text="About Our Team" />
      {username !== null ? (
        <Fragment>
          <NavLink url={"/user-page.html?user=" + username} text="Your Page" />
          <NavLink url="/place/add" text="Add Place" />
          <NavLink url="/community.html" text="Community" />
          <NavLink url="/logout" text="Logout" />
        </Fragment>
      ) : (
        <NavLink url="/login" text="Login" />
      )}
    </nav>
  );
}

function NavLink({ url, text, style }) {
  return (
    <div style={{ display: "inline", margin: 10, float: "right", ...style }}>
      <a href={url}>{text}</a>
    </div>
  );
}

export default Navigation;
