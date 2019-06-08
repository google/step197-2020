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

  fetchUserList() {
    const url = "/user-list";
    fetch(url)
      .then(response => {
        return response.json();
      })
      .then(users => {
        this.setState({ Users: users });
        console.log(users);
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

/*
<li>
  <a href={"/user-page.html?user="+ email}> {name} {email}
  </a>
</li>
*/
/** Fetches users and adds them to the page. */

/*
 * Builds a list element that contains a link to a user page, e.g.
 * <li><a href="/user-page.html?user=test@example.com">test@example.com</a></li>
 */
/*function buildUserListItem(user) {
  const userLink = document.createElement("a");
  userLink.setAttribute("href", "/user-page.html?user=" + user);
  userLink.appendChild(document.createTextNode(user));
  const userListItem = document.createElement("li");
  userListItem.appendChild(userLink);
  return userListItem;
}

*/
/** Fetches data and populates the UI of the page. */
