import React from "react";
import "./QuizButton.css";

const QuizButton = ({ text, handleClick }) => {
  return (
    <div>
      <button onClick={handleClick} id="quizButton">
        {text}
      </button>
    </div>
  );
};

export default QuizButton;
