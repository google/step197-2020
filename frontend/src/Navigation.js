/**
 * Creates navigation depending on whether user is logged in or not
 */

import React, { Fragment, useEffect, useState } from "react";

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

  if (username === null) {
    return null;
  }

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
      <NavLink url="/" text="Home" />
      <NavLink url="/aboutus.html" text="About Our Team" />
      {username != undefined ? (
        <Fragment>
          <NavLink url={"/user-page.html?user=" + username} text="Your Page" />
          <NavLink url="/place/add" text="Add Place" />
          <NavLink url="/community" text="Community" />
          <NavLink url="/logout" text="Logout"/>
        </Fragment>
      ) : (
        <NavLink url="/login " text="Login" />
      )}
    </nav>
  );
}

function NavLink({ url, text, style }) {
  return (
    <span style={{ margin: 10, ...style }}>
      <a href={url}>{text}</a>
    </span>
  );
}

export default Navigation;
