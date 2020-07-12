import React from "react";

// displays a question and an edit and delete button
const Question = ({ question, handleDelete, handleEdit, id }) => {
  return (
    <div style={styles.question}>
      <div className="fourteen wide column" id="questionPreview">
        {question}
      </div>
      <div className="two wide column" id="editButtons">
        <button style={styles.editButtons} className="ui negative button" onClick={() => handleDelete(id)}>
          Delete
        </button>
        <button style={styles.editButtons} className="ui positive button" onClick={() => handleEdit(id)}>
          Edit
        </button>
      </div>
    </div>
  );
};

const styles = {
  question: {
    color: "#181717",
    margin: "10px 10px 20px 10px",
    backgroundColor: "#F7F6F5",
    boxShadow:
      "0 1px 4px rgba(0, 0, 0, 0.3), 0 0 40px rgba(0, 0, 0, 0.1) inset",
    display: "flex",
    flexDirection: "row"
  },
  editButtons: {
    float: "right",
    margin: "0.5vw 0.5vw"
  }
};

export default Question;
