import React from "react";
import renderer from "react-test-renderer";
import About from "../homePage/About";
import LandingPage from "../homePage/LandingPage";
import NavBar from "../homePage/NavBar";
import Header from "../main-components/Header";
import SideBar from "../main-components/Sidebar";
import CreateCard from "../pages/CreateCard";
import CreateFolder from "../pages/CreateFolder";
import MyFolders from "../pages/MyFolders";
import InsideFolder from "../pages/InsideFolder";
import NotFound from "../pages/404";
import { MemoryRouter } from "react-router-dom";

/**
 * Snapshot testing for home page
 */
describe("Home page", () => {
  test("snapshot About renders", () => {
    const component = renderer.create(<About />);
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot Landing Page renders", () => {
    const component = renderer.create(<LandingPage />);
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot Navigation Bar renders", () => {
    const component = renderer.create(<NavBar />);
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });
});

/**
 * Snapshot testing for main components
 */
describe("mainComponents", () => {
  test("snapshot header renders", () => {
    const component = renderer.create(<Header />);
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot SideBar renders", () => {
    const component = renderer.create(
      <MemoryRouter>
        <SideBar />
      </MemoryRouter>
    );
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });
});

/**
 * Snapshot testing for main App pages
 */
describe("mainApp", () => {
  test("snapshot My Folders renders", () => {
    const component = renderer.create(
      <MemoryRouter>
        <MyFolders />
      </MemoryRouter>
    );
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot CreateCard renders", () => {
    const component = renderer.create(
      <MemoryRouter>
        <CreateCard />
      </MemoryRouter>
    );
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  // Must define window.alert for jest
  window.alert = jest.fn();
  test("snapshot Create Folder renders", () => {
    window.alert.mockClear();
    const component = renderer.create(
      <MemoryRouter>
        <CreateFolder />
      </MemoryRouter>
    );
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot Inside Folder renders", () => {
    const component = renderer.create(
      <MemoryRouter>
        <InsideFolder />
      </MemoryRouter>
    );
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("snapshot 404 renders", () => {
    const component = renderer.create(
      <MemoryRouter>
        <NotFound />
      </MemoryRouter>
    );
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });
});
