import Enzyme, { configure } from "enzyme";
import React from 'react';
import Adapter from "enzyme-adapter-react-16";
import fetch from "node-fetch";
import { mount, shallow } from 'enzyme';
import LandingPage from "../homePage/LandingPage"
import NavBar from "../homePage/NavBar"

Enzyme.configure({ adapter: new Adapter() });
global.fetch = fetch;

describe('Home Page component testing', () => {
  test("Landing page loads button with alternating text", () => {
    const wrapper = mount(<LandingPage loginStatus={true} />);
    expect(wrapper.find('.MyButton').text()).toEqual(" My Folders ");
  });

  test("Nav Bar generates new tab when logged in", () => {
    const wrapper = mount(<NavBar loginStatus={true} />);
    const button = wrapper.find('.MyButton').at(0);
    expect(button.text()).toEqual(" My Folders ")
  });

   test("Nav Bar generates logout button when logged in", () => {
     const wrapper = mount(<NavBar loginStatus={true} />);
     const button = wrapper.find(".MyButton").at(1);
     expect(button.text()).toEqual(" Logout ");
   });
   // If there were two buttons this test would find them both and crash
  test("Nav Bar does not generate my folders tab when logged out", () => {
    const wrapper = mount(<NavBar loginStatus={false} />);
    expect(wrapper.find(".MyButton").text()).toEqual(" Login ");
  });
});
