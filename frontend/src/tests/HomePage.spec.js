import React from "react";
import renderer from "react-test-renderer";
import LandingPage from "../homePage/LandingPage";
import NavBar from "../homePage/NavBar";

describe("Home Page component testing", () => {
  test("Landing page loads button with alternating text", () => {
    const component = renderer.create(<LandingPage loginStatus={true} />);
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("Nav Bar generates new tab when logged in", () => {
    const component = renderer.create(<NavBar loginStatus={true} />);
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  test("Nav Bar does not generate my folders tab when logged out", () => {
    const component = renderer.create(<NavBar loginStatus={false} />);
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });
});
