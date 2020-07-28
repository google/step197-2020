import React from "react";
import styled from "@emotion/styled";
import { Link } from "react-router-dom";

const SideIcon = (props) => {
  const Container = styled.div`
    width: 80%;
    margin-top: 15%;
  `;
  const Image = styled.img`
    width: 100%;
    &:hover {
        color: #blue;
    }
  `;

  return (
    <Container>
      <Link to={props.link}>
        <Image src={props.icon} alt="icon option"></Image>
      </Link>
    </Container>
  );
};

export default SideIcon;
