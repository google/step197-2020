/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const React = window.React;
const ReactDOM = window.ReactDOM;
const h = React.createElement;

class UserPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = { input: "" };
    this.fetchData = this.fetchData.bind(this);
  }

  fetchData() {
    const url = `/about?user=${this.props.userEmail}`;
    fetch(url)
      .then(res => {
        return res.text();
      })
      .then(currentValue => {
        this.setState({ currentValue });
      });
  }

  componentDidMount() {
    this.fetchData();
  }

  render() {
    // Pure function
    const button = h(
      "button",
      {
        onClick: () =>
          fetch("/about", {
            method: "POST",
            credentials: "same-origin",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `about-me=${encodeURIComponent(this.state.input)}`
          }).then(() => window.location.reload())
      },
      "Submit"
    );

    return h(
      "div",
      { id: "about-me" },
      h("div", {
        id: "user-text",
        dangerouslySetInnerHTML: { __html: this.state.currentValue }
      }),
      h("textarea", {
        name: "about-me",
        placeholder: "about me",
        rows: "4",
        required: true,
        value: this.state.input,
        onChange: e => {
          this.setState({ input: e.target.value });
        }
      }),
      button
    );
  }
}

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get("user");

ReactDOM.render(
  h(UserPage, { userEmail: parameterUsername }),
  document.getElementById("root")
);

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace("/");
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById("page-title").innerText = parameterUsername;
  document.title = parameterUsername + " - User Page";
}

setPageTitle();
