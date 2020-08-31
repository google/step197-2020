import React from "react";
import styled from "@emotion/styled";
import css from "./Search.css";

const Search = (props) => {
  

  return (
      <div id="searchContainer">
        <form id="searchForm" method="GET" action="/SimilarWords">
          <button id="searchButton" type="submit" className="material-icons">
          <i className="material-icons">search</i>
          </button>
        <input id="searchBar" type="text" placeholder="Search for similar words..." name="queryWord"></input>
        <input id="searchNum" type="number" name="numOfWordsRequested" min="1" max="45" defaultValue="10"></input>
        </form>
      </div>
  );
};

export default Search;
