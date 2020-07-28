import React from "react";
import styled from "@emotion/styled";
import accountIcon from "../SVG/account.svg";
import newCardIcon from "../SVG/create-24px.svg"
import objectIcon from "../SVG/image_search-24px.svg";
import youTubeIcon from "../SVG/youtube.svg";
import newFolderIcon from "../SVG/create_new_folder-24px.svg"
import homeIcon from "../SVG/home-24px.svg";
import SideIcon from "../sub-components/SideIcon";

const Sidebar = (props) => {
  let sideStyling = "width: 0; border: none;";
  if (props.bool === "t") {
     sideStyling = "width: 20%; border: .3rem solid black;"
  }
  
   const Side = styled.div`
     flex: 1;
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
    <Side id="side" class="flex-container">
      <SideIcon link="/" icon={homeIcon}></SideIcon>
      <SideIcon link="/CreateCard" icon={newCardIcon}></SideIcon>
      <SideIcon link="/CreateFolder" icon={newFolderIcon}></SideIcon>
      <SideIcon link="/ImageInterface" icon={objectIcon}></SideIcon>
      <SideIcon link="/YoutubeInterface" icon={youTubeIcon}></SideIcon>
      <SideIcon link="/noneyet" icon={accountIcon}></SideIcon>
    </Side>
  );
};

export default Sidebar;
