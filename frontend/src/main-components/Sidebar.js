import React from "react";
import styled from "@emotion/styled";
import logoutIcon from "../SVG/login-black-18dp.svg";
import newCardIcon from "../SVG/create-24px.svg";
import objectIcon from "../SVG/image_search-24px.svg";
import gameIcon from "../SVG/game.svg";
import newFolderIcon from "../SVG/create_new_folder-24px.svg";
import homeIcon from "../SVG/home-24px.svg";
import SideIcon from "../sub-components/SideIcon";

const InvisibleSide = styled.div`
  flex: 1;
  max-width: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  border: 1rem solid white;
  border-radius: 1rem;
  margin: 1%;
  @media (max-width: 700px) {
    width: 0;
    border: none;
    overflow-x: hidden;
    position: fixed;
  }
`;

const VisibleSide = styled(InvisibleSide)`
  @media (max-width: 700px) {
    width: 20%;
    border: 0.3rem solid black;
    align-items: flex-start;
    overflow-x: hidden;
    position: fixed;
  }
`;

const Sidebar = (props) => {
  if (props.visible) {
    return (
      <VisibleSide id='side' className='flex-container'>
        <SideIcon link='/MyFolders' icon={homeIcon}></SideIcon>
        <SideIcon link='/CreateCard' icon={newCardIcon}></SideIcon>
        <SideIcon link='/CreateFolder' icon={newFolderIcon}></SideIcon>
        <SideIcon link='/ImageInterface' icon={objectIcon}></SideIcon>
        <SideIcon link='/StudyMode' icon={gameIcon}></SideIcon>
        <SideIcon link='/homePage' icon={logoutIcon}></SideIcon>
      </VisibleSide>
    );
  } else {
    return (
      <InvisibleSide id='side' className='flex-container'>
        <SideIcon link='/MyFolders' icon={homeIcon}></SideIcon>
        <SideIcon link='/CreateCard' icon={newCardIcon}></SideIcon>
        <SideIcon link='/CreateFolder' icon={newFolderIcon}></SideIcon>
        <SideIcon link='/ImageInterface' icon={objectIcon}></SideIcon>
        <SideIcon link='/StudyMode' icon={gameIcon}></SideIcon>
        <SideIcon link='/homePage' icon={logoutIcon}></SideIcon>
      </InvisibleSide>
    );
  }
};

export default Sidebar;
