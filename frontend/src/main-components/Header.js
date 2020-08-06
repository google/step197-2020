import React from "react";
import styled from "@emotion/styled";
import Search from "../sub-components/Search";
import logo from "../SVG/Logo.jpg";
import "material-design-icons";

const Header = (props) => {
  
  const Head = styled.div`
    display: flex;
    align-items: center;
    border: 1rem solid white;
    border-radius: 1rem;
    background-color: white;
    margin 1%;
    @media (max-width: 700px) {
        text-align: left;
    }
  `;
  const Image = styled.img`
    width: 70%;
    padding-right: 15%;
    padding-left: 15%;
  `;
  const Box = styled.div`
    flex: 2;
    @media (max-width: 700px) {
      flex: 1;
      margin-right: 0.5rem;
    }
  `;
  const Menu = styled.button`
    background-color: white;
    height: 100%;
    padding-right: none;
    border: none;
  `;

  let display = <Image src={logo} alt="Frame Cards Logo"></Image>;
  /*
   * If the device size is less than 700px wide then we will display
   * a button for our side menu instead of our logo.
   */
  const width = self.innerWidth;
  if (width < 700) {
    display = (
      <Menu onClick={(e)=> props.handleClick()}>
        <i className="material-icons">menu</i>
      </Menu>
    );
  }
    return (
      <Head className="flex-container">
        <Box>{display}</Box>
        <Search></Search>
      </Head>
    );
};


export default Header;
