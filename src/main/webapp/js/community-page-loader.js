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

  /** Fetches users from /user-list */
  fetchUserList() {
    const url = "/user-list";
    fetch(url)
      .then(response => {
        return response.json();
      })
      .then(users => {
        this.setState({ users: users });
      });
  }

  componentDidMount() {
    this.fetchUserList();
  }

  /** Adds list of community users to community.html as a list element */
  render() {
    const { users } = this.state;
    if (users == null) {
      return h("div", null, "No Users Have Posted Anything");
    }
    const children = [];
    for (const user of users) {
      children.push(h(CommunityUser, { email: user, key: user }));
    }
    return h("ul", null, children);
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
