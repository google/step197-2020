import Enzyme, { configure } from "enzyme";
import React from "react";
import Adapter from "enzyme-adapter-react-16";
import fetch from "node-fetch";
import { mount} from "enzyme";
import CreateFolderContent from "../main-components/CreateFolderContent";

Enzyme.configure({ adapter: new Adapter() });
global.fetch = fetch;

describe('Creating Folder Page testing', () => {
  test("When typing folderName live preview is available", () => {
    const wrapper = mount(<CreateFolderContent />);
    wrapper.find('#folderName').simulate('blur', { target: { value: 'Verbs' } });
    expect(wrapper.find('#previewFolderName').text()).toEqual("Verbs")
  });

  test("When selecting a language live preview is displayed", () => {
    const wrapper = mount(<CreateFolderContent />);
    wrapper.find("#language").simulate("change", { target: { value: "Spanish" } });
    expect(wrapper.find("#previewFolderLang").text()).toEqual("Spanish");
  });
  // TODO: When our website is fully completed we should be rerouted to an empty create Folder page
  test("Checking Submit button for form", () => {
    const submitted = jest.fn();
    const wrapper = mount(<CreateFolderContent />);
    const result = wrapper.find('#myForm').simulate('submit');
    expect(result.exists()).toEqual(true);
  });
});