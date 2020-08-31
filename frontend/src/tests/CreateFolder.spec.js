import Enzyme from "enzyme";
import React from "react";
import Adapter from "enzyme-adapter-react-16";
import fetch from "node-fetch";
import { mount } from "enzyme";
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
    wrapper.find("#folderDefaultLanguage").simulate("change", { target: { value: "Spanish" } });
    expect(wrapper.find("#previewFolderLang").text()).toEqual("Spanish");
  });
});