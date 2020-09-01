import React from "react";

const frontStyle = {
  borderRadius: "30px",
  position: "absolute",
  width: "300px",
  height: "350px",
  backgroundColor: "white",
  color: "black",
  boxShadow: "rgba(0, 0, 0, 0.2) 0px 1px 8px",
  fontFamily: "'Montserrat', sans-serif",
  fontSize: "30px",
  fontWeight: "600",
  textAlign: "center",
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
  alignItems: "center",
};

const imgStyle = {
  display: "block",
  maxWidth: "150px",
  maxHeight: "150px",
  width: "auto",
  height: "auto",
  margin: "10px",
};

function FlashcardFront(props) {
  return (
    <div className='FlashcardFront' style={frontStyle}>
      {props.image ? (<img style={imgStyle} src={`/serve?key=${props.image}`} alt={props.text}/>) : null}
      {props.text}
    </div>
  );
}

export default FlashcardFront;