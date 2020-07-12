import React, { useState } from "react";
import api from "../utils/api";

// form for adding a question to the database
const AddQuestionForm = ({
  questionText,
  correctAnswer,
  wrong_1,
  wrong_2,
  wrong_3,
  author,
  handleFormChange
}) => {

  // confirmation messages that appear after submitting a question 
  const [confirmationMessage, setConfirmationMessage] = useState("");

  const hideConfirmation = time => {
    setTimeout(() => setConfirmationMessage(""), time);
  };

  // save input to the form field as a question in the database
  const handleSubmit = event => {
    event.preventDefault();
    // clear text fields after submit
    handleFormChange({ type: "question", payload: "" });
    handleFormChange({ type: "correctAnswer", payload: "" });
    handleFormChange({ type: "wrong_1", payload: "" });
    handleFormChange({ type: "wrong_2", payload: "" });
    handleFormChange({ type: "wrong_3", payload: "" });
    handleFormChange({ type: "author", payload: "" });

    // bring the data into the format, that the database schema requires
    let question = {
      question: questionText,
      correct_answer: correctAnswer,
      incorrect_answers: [wrong_1, wrong_2, wrong_3],
      author: author
    };

    // save the question to the database
    api
      .post("/questions", question)
      .then(setConfirmationMessage("Added custom question."))
      .then(hideConfirmation(3000))
      .catch(() => {
        setConfirmationMessage("An error occured.");
        hideConfirmation(3000);
      });
  };

  // load in questions from external api
  const loadQuestions = event => {
    event.preventDefault();
    api
      .get("/api")
      .then(res =>
        res.status === 200
          ? "Loaded in external questions."
          : "An error occured."
      )
      .then(message => setConfirmationMessage(message))
      .then(hideConfirmation(3000));
  };

  return (
    <div style={styles.addQuestion}>
      <form className="ui form" autoComplete="off">
        <div className="field">
          <label>Question</label>
          <input
            type="text"
            name="question"
            value={questionText}
            onChange={event =>
              handleFormChange({
                type: "question",
                payload: event.currentTarget.value
              })
            }
          />
        </div>
        <div className="field">
          <label>Correct Answer</label>
          <input
            type="text"
            name="correctAnswer"
            value={correctAnswer}
            onChange={event =>
              handleFormChange({
                type: "correctAnswer",
                payload: event.currentTarget.value
              })
            }
          />
        </div>
        <div className="field">
          <label>Wrong Answers</label>
          <div className="three fields">
            <div className="field">
              <input
                type="text"
                name="wrong_1"
                value={wrong_1}
                onChange={event =>
                  handleFormChange({
                    type: "wrong_1",
                    payload: event.currentTarget.value
                  })
                }
              />
            </div>
            <div className="field">
              <input
                type="text"
                name="wrong_2"
                value={wrong_2}
                onChange={event =>
                  handleFormChange({
                    type: "wrong_2",
                    payload: event.currentTarget.value
                  })
                }
              />
            </div>
            <div className="field">
              <input
                type="text"
                name="wrong_3"
                value={wrong_3}
                onChange={event =>
                  handleFormChange({
                    type: "wrong_3",
                    payload: event.currentTarget.value
                  })
                }
              />
            </div>
          </div>
        </div>
        <div className="field">
          <label>Author</label>
          <input
            type="text"
            name="author"
            value={author}
            onChange={event =>
              handleFormChange({
                type: "author",
                payload: event.currentTarget.value
              })
            }
          />
        </div>
        <div style={styles.loadButtons}>
          <button
            type="submit"
            onClick={handleSubmit}
            className="ui violet button"
          >
            Add Custom Question
          </button>
          <button onClick={loadQuestions} className="ui violet button">
            Load in External Questions
          </button>
        </div>
        <div style={styles.confirmationMessage}>{confirmationMessage}</div>
      </form>
    </div>
  );
};

const styles = {
  addQuestion: {
    fontsize: "2rem",
    backgroundColor: "#F7F6F5",
    boxShadow:
      "0 1px 4px rgba(0, 0, 0, 0.3), 0 0 40px rgba(0, 0, 0, 0.1) inset",
    borderRadius: "25px",
    padding: "20px",
    color: "#181717"
  },
  loadButtons: {
    display: "flex",
    justifyContent: "space-evenly"
  },
  confirmationMessage: {
    minHeight: "1.9rem",
    color: "red",
    textAlign: "center",
    fontSize: "0.8em",
    marginTop: "1vw"
  }
};

export default AddQuestionForm;
