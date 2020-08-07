import React from 'react';
import renderer from "react-test-renderer";

import About from "./homePage/About";
import LandingPage from "./homePage/LandingPage";
import NavBar from "./homePage/NavBar";
import Header from "./main-components/Header";
import SideBar from "./main-components/Sidebar";
import CreateCard from "./pages/CreateCard";
import CreateFolder from "./pages/CreateFolder";
import MyFolders from "./pages/MyFolders";
import InsideFolder from "./pages/InsideFolder";
import NotFound from "./pages/404";

/**
 * Snapshot testing for home page
 */
describe('Home page', () => {
  test('snapshot About renders', () => {
    const component = renderer.create(<About/>);
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot Landing Page renders", () => {
    const component = renderer.create(<LandingPage />);
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot Navigation Bar renders", () => {
    const component = renderer.create(<NavBar />);
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });
});

/**
 * Snapshot testing for main components
 */
describe("mainComponents", () => {
  test("snapshot header renders", () => {
    const component = renderer.create(<Header />);
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot SideBar renders", () => {
    const component = renderer.create(<SideBar />);
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });
});

/**
 * Snapshot testing for main App pages
 */
describe('mainApp', () => {
  test("snapshot My Folders renders", () => {
    const component = renderer.create(<MyFolders />);
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot CreateCard renders", () => {
    const component = renderer.create(<CreateCard />);
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

   test("snapshot Create Folder renders", () => {
     const component = renderer.create(<CreateFolder />);
     let tree = component.toJSON();
     expect(tree).toMatchSnapshot();
   });

   test("snapshot Inside Folder renders", () => {
     const component = renderer.create(<InsideFolder />);
     let tree = component.toJSON();
     expect(tree).toMatchSnapshot();
   });
  
  test("snapshot 404 renders", () => {
    const component = renderer.create(<NotFound />);
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });
});

