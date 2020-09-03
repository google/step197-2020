import React from "react";
import { css } from "@emotion/core";
import PulseLoader from "react-spinners/PulseLoader";

class TranslationLoading extends React.Component {
  render() {
    if (this.props.loading) {
      return (
        <div>
          <PulseLoader size={7} margin={3} color={"#136F9F"} loading={true} />
        </div>
      );
    }
    return <div></div>;
  }
}

export default TranslationLoading;