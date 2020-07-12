import React from "react";

// displays how many questions from the total number of questions are already answered
const Progress = ({ current, finish }) => {
  return (
    <div style={styles.progress}>
      {current} / {finish}
    </div>
  );
};

const styles = {
  progress: {
    display: "flex",
    flexDirection: "row",
    justifyContent: "space-evenly",
    alignItems: "center",
    border: "1px solid white",
    borderRadius: "10px",
    fontSize: "3vw",
    width: "15vw",
    padding: "7px",
    margin: "0 10px"
  }
};

export default Progress;
