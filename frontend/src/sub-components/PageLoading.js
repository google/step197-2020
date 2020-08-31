import React from "react";
import { css } from "@emotion/core";
import ClipLoader from "react-spinners/ClipLoader"

class PageLoading extends React.Component {
    render() {
        return (
            <div>
                <ClipLoader size={100} color={"#123abc"} loading={true} />
            </div>
        );
    }
}