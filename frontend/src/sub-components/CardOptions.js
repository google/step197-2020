import React from "react";
import styled from "@emotion/styled";

const Option = styled.div`
  display: flex;
  align-items: center;
  width: 80%;
  background-color: #d3d3d3;
  border-radius: 5rem;
  height: 10rem;
  justify-content: flex-start;
  &:hover {
    border: 0.25rem solid #136f9f;
  }
`;

const Input = styled.input`
  margin-left: 5%;
  width: 3.5rem;
  height: 3.5rem;
  background: #a9a9a9;
`;

const Label = styled.label`
  font-size: 4rem;
  margin-right: auto;
  margin-left: auto;
`;

class CardOptions extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return this.props.options.map((word) => {
      return (
        <Option key={word}>
          <Input
            type='radio'
            id={word}
            name='CardOption'
            value={word}
            onChange={this.props.func}
          />
          <Label>{word}</Label>
        </Option>
      );
    });
  }
}

export default CardOptions;