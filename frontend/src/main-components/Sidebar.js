import React from "react";
import styled from "@emotion/styled";
import logoutIcon from "../SVG/login-black-18dp.svg";
import newCardIcon from "../SVG/create-24px.svg";
import objectIcon from "../SVG/image_search-24px.svg";
import gameIcon from "../SVG/game.svg";
import newFolderIcon from "../SVG/create_new_folder-24px.svg";
import homeIcon from "../SVG/home-24px.svg";
import SideIcon from "../sub-components/SideIcon";

const Sidebar = (props) => {
  let sideStyling = "width: 0; border: none;";
  if (props.bool) {
    sideStyling =
      "width: 20%; border: .3rem solid black; align-items:flex-start;";
  }

  const Side = styled.div`
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
      ${sideStyling}
      overflow-x: hidden;
      position: fixed;
    }
  `;

  return (
    <Side id='side' className='flex-container'>
      <SideIcon link='/myFolders' icon={homeIcon}></SideIcon>
      <SideIcon link='/CreateCard' icon={newCardIcon}></SideIcon>
      <SideIcon link='/CreateFolder' icon={newFolderIcon}></SideIcon>
      <SideIcon link='/ImageInterface' icon={objectIcon}></SideIcon>
      <SideIcon link='/StudyMode' icon={gameIcon}></SideIcon>
      <SideIcon link='/myFolders' icon={logoutIcon}></SideIcon>
    </Side>
  );
};

export default Sidebar;