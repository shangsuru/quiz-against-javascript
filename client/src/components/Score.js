import React from "react";
import { FaUserAlt, FaJs } from "react-icons/fa";

// displays the current points for the human player and the question answering system
const Score = ({ humanPoints, jsPoints }) => {
  return (
    <div style={styles.score}>
      <FaUserAlt size={"3vw"} />
      <div>{humanPoints}</div>
      <FaJs size={"3vw"} />
      <div>{jsPoints}</div>
    </div>
  );
};

const styles = {
  score: {
    display: "flex",
    flexDirection: "row",
    justifyContent: "space-evenly",
    alignItems: "center",
    border: "1px solid white",
    borderRadius: "10px",
    fontSize: "3vw",
    width: "17vw",
    padding: "7px",
    margin: "0 10px"
  }
};

export default Score;
