import React from "react";
import styled from "@emotion/styled";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";

const SideIcon = (props) => {
  const Container = styled.div`
    width: 80%;
    margin-top: 15%;
    @media (max-width: 700px) {
      margin-left: 5%;
      margin-right: 5%;
    }
  `;
  const Image = styled.img`
    width: 100%;
    border: 0.3rem solid #136f9f;
    border-radius: 1rem;
    background-color: #136f9f;
    &:hover {
      color: #blue;
    }
  `;
 
  return (
    <Container>
      <Link to={props.link}>
        <motion.div whileHover={{ scale: 1.2 }}>
          <Image src={props.icon} alt="icon option"></Image>
        </motion.div>
      </Link>
    </Container>
  );
};

export default SideIcon;