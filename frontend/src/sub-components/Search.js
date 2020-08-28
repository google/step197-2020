import React from "react";
import styled from "@emotion/styled";

const Search = (props) => {
    const Container = styled.div`
      @import "~material-design-icons/iconfont/material-icons.css";
      position: relative;
      display: flex;
      flex: 8;
      align-items: center;
      justify-content: center;
    `;
    
    const Button = styled.button`
      width: 14%;
      border-radius: 2rem 0 0 2rem;
      border: 0.25rem solid #1D303D;
      background-color: #136F9F;
      color: white;
      @media (max-width: 700px) {
        width: 20%;
      }
    `;

    const Form = styled.form`
      width: 50%;
      position: relative;
      display: flex;
      @media (max-width: 700px) {
        width: 100%;
        height: 2.75rem;
      }
    `;

    const Input = styled.input`
      background-color: #c4c4c4;
      border-radius: 0 2rem 2rem 0;
      border: 0.25rem solid #1d303d;
      border-left: none;
      outline: none;
      width: 60%;
      font-size: 3rem;
      @media (max-width: 700px) {
        width: 70%;
        font-size: 2rem;
      }
      &:focus {
        background-color: white;
        text-decoration-color: black;
        box-shadow: 5px 5px 20px  1px blue;
      }
    `;

  return (
      <Container>
        <Form action="/someServlet">
          <Button type="submit" className="material-icons">
          <i className="material-icons">search</i>
          </Button>
          <Input type="text" placeholder="Search.." name="search-input"></Input>
        </Form>
      </Container>
  );
};

export default Search;
