import React from "react";
import { css } from "@emotion/core";
import HashLoader from "react-spinners/HashLoader"

class PageLoading extends React.Component {
    render() {
        return (
          <div>
            <HashLoader size={100} color={"#136F9F"} loading={true} />
          </div>
        );
    }
}

export default PageLoading;