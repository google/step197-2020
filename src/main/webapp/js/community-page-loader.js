const React = window.React;
const ReactDOM = window.ReactDOM;
const h = React.createElement;
const root = document.getElementById("list");

class Community extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
    this.fetchUserList = this.fetchUserList.bind(this);
  }
  /** Adds list of community users to community.html as a list element */
  render() {
    const { Users } = this.state;
    if (Users == null) {
      return h("li", null, "loading...");
    }
    const children = [];
    for (const user of Users) {
      children.push(h(CommunityUser, { email: user }));
    }
    return h("li", null, children);
  }

  /** Fetches users from /user-list */
  fetchUserList() {
    const url = "/user-list";
    fetch(url)
      .then(response => {
        return response.json();
      })
      .then(users => {
        this.setState({ Users: users });
      });
  }

  componentDidMount() {
    this.fetchUserList();
    this.timer = window.setInterval(this.fetchUserList, 500);
  }
  componentWillUnmount() {
    window.clearInterval(this.timer);
  }
}

class CommunityUser extends React.Component {
  render() {
    const email = this.props.email;
    return h(
      "li",
      null,
      h("a", { href: "/user-page.html?user=" + email }, email)
    );
  }
}

ReactDOM.render(h(Community), root);
