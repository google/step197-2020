import React from "react";
import ReactDOM from "react-dom";

function MessageFeed() {
  const [messages, setMessages] = React.useState([]);
  React.useEffect(fetchMessages, []);

  // Fetch messages and add them to the page.
  function fetchMessages() {
    const url = "/api/feed";
    fetch(url)
      .then(response => response.json())
      .then(messages => setMessages(messages))
      .catch(error => console.log(error.message));
  }

  // Store message counts in JS Map with emails as the key
  const messageCount = new Map();
  for (const message of messages) {
    const { user } = message;
    if (!messageCount.has(user)) {
      messageCount.set(user, 0);
    }

    messageCount.set(user, messageCount.get(user) + 1);
  }

  return (
    <div>
      {Array.from(messageCount.entries()).map(([user, count]) => {
        return <NumUserMessages user={user} count={count} />;
      })}
      {Array.from(messages).map(message => {
        return <MessageDiv {...message} key={message.id} />;
      })}
    </div>
  );
}

function MessageDiv({ user, text, timestamp }) {
  const date = new Date(timestamp).toString();

  return (
    <div className="message-div">
      <div className="message-header">
        <div className="left-align">{user}</div>
        <div className="right-align">{date}</div>
      </div>
      <div className="message-body">{text}</div>
    </div>
  );
}

function NumUserMessages({ user, count }) {
  return (
    <div>
      {user} has posted {count} messages:{" "}
    </div>
  );
}

function App() {
  return (
    <React.Fragment>
      <h1>Message Feed</h1>
      <hr />
      <MessageFeed />
    </React.Fragment>
  );
}

// Fetch data and populate the UI of the page.
const root = document.getElementById("root");
ReactDOM.render(<App />, root);
