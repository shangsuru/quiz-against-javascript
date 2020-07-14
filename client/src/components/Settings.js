import React, { useEffect, useReducer } from "react";
import AddQuestionForm from "./AddQuestionForm";
import Question from "./Question";
import api from "../utils/api";
import "./Settings.css";

const reducer = (state, action) => {
  switch (action.type) {
    case "questionList":
      return { ...state, questionList: action.payload };
    case "question":
      return { ...state, question: action.payload };
    case "correctAnswer":
      return { ...state, correctAnswer: action.payload };
    case "wrong_1":
      return { ...state, wrong_1: action.payload };
    case "wrong_2":
      return { ...state, wrong_2: action.payload };
    case "wrong_3":
      return { ...state, wrong_3: action.payload };
    case "author":
      return { ...state, author: action.payload };
    case "confirmationMessage":
      return { ...state, confirmationMessage: action.payload };
    default:
      return state;
  }
};

const Settings = () => {
  // contains list of questions and the content of the text input fields
  const [state, dispatch] = useReducer(reducer, {
    questionList: [],
    question: "",
    correctAnswer: "",
    wrong_1: "",
    wrong_2: "",
    wrong_3: "",
    author: "",
    confirmationMessage: ""
  });

  // load in questions at startup
  useEffect(() => {
    api
      .get("/questions")
      .then(result => dispatch({ type: "questionList", payload: result.data }));
  }, []);

  // remove the question from the list and from the database
  const handleDelete = id => {
    dispatch({
      type: "questionList",
      payload: state.questionList.filter(question => question.id !== id)
    });
    api.delete(`/questions/${id}`);
    if (state.questionList.length === 1) {
      api
        .get("/questions")
        .then(result =>
          dispatch({ type: "questionList", payload: result.data })
        );
    }
  };

  // delete the question and load the data of the question into the text fields for editing
  const handleEdit = id => {
    let questionToBeEdited = state.questionList.filter(
      question => question.id === id
    )[0];
    dispatch({ type: "question", payload: questionToBeEdited.question });
    dispatch({
      type: "correctAnswer",
      payload: questionToBeEdited.correct_answer
    });
    dispatch({
      type: "wrong_1",
      payload: questionToBeEdited.incorrect_answers[0]
    });
    dispatch({
      type: "wrong_2",
      payload: questionToBeEdited.incorrect_answers[1]
    });
    dispatch({
      type: "wrong_3",
      payload: questionToBeEdited.incorrect_answers[2]
    });
    dispatch({ type: "author", payload: questionToBeEdited.author });
    handleDelete(id);
  };

  // construct the list of questions for editing and deleting
  const renderedList = state.questionList.map(question => {
    return (
      <Question
        handleDelete={handleDelete}
        handleEdit={handleEdit}
        key={question.id}
        question={question.question}
        id={question.id}
      />
    );
  });

  return (
    <div className="row">
      <div className="column">
        <AddQuestionForm
          questionText={state.question}
          correctAnswer={state.correctAnswer}
          wrong_1={state.wrong_1}
          wrong_2={state.wrong_2}
          wrong_3={state.wrong_3}
          author={state.author}
          handleFormChange={dispatch}
        />
      </div>
      <div id="questionList" className="column ui relaxed divided list">
        {renderedList}
      </div>
    </div>
  );
};

export default Settings;
