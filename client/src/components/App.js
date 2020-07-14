import React, { useState, useEffect, useRef } from "react";
import { FaCog, FaRedoAlt, FaArrowUp } from "react-icons/fa";
import Progress from "./Progress";
import QuestionEdit from "./Settings";
import QuizButton from "./QuizButton";
import Score from "./Score";
import api from "../utils/api";
import axios from "axios";
import "./App.css";

const App = () => {
  const [questions, setQuestions] = useState([]);
  const [humanPoints, setHumanPoints] = useState(0);
  const [jsPoints, setJsPoints] = useState(0);
  const [background, setBackground] = useState("blue");
  const [currentQuestionNumber, setCurrentQuestionNumber] = useState(0);
  const [gameStatus, setGameStatus] = useState("init");
  const [currentAnswers, setCurrentAnswers] = useState([]);
  const [jsAnswer, setJsAnswer] = useState("");

  // smooth scrolling effect
  const settingsRef = useRef(null);
  const quizRef = useRef(null);

  const scroll = ref => {
    ref.current.scrollIntoView({ behavior: "smooth" });
  };

  const shuffle = a => {
    for (let i = a.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [a[i], a[j]] = [a[j], a[i]];
    }
    return a;
  };

  // distribute answers randomly on buttons
  const getAnswers = question => {
    let answers = [];
    answers.push(question.correct_answer);
    question.incorrect_answers.forEach(text => {
      answers.push(text);
    });
    return shuffle(answers);
  };

  // question answering bot using google search api
  const getJsAnswer = async () => {
    // use google's api to search question
    let currentQuestion = questions[currentQuestionNumber - 1];
    try {
      let query = currentQuestion.question;
      let result = await axios.get(
        "https://www.googleapis.com/customsearch/v1",
        {
          params: {
            key: "AIzaSyDux8DSxZpUymlzcxqU9ObOOMAFczTLVYk",
            cx: "013674424878579071651:jxmh1kr1hmn",
            q: query
          }
        }
      );
      // go through search results and gather page title and search-preview snippets
      let searchResults = result.data.items;
      let resultString = "";
      searchResults.forEach(item => {
        resultString += item.title;
        resultString += item.snippet;
      });
      // map each answer to its frequency in the result string
      let frequencies = currentAnswers.map(
        answer => (resultString.match(new RegExp(answer, "g")) || []).length
      );
      // choose answer with highest frequency and increment point if it was correct
      let indexOfMostFrequent = frequencies.indexOf(Math.max(...frequencies));
      let chosenAnswer = currentAnswers[indexOfMostFrequent];
      setJsAnswer(chosenAnswer);
      if (chosenAnswer === currentQuestion.correct_answer) {
        setJsPoints(jsPoints + 1);
      }
    } catch (error) {
      // fallback option if api is not reachable: random choice
      let chosenAnswer = currentAnswers[Math.floor(Math.random() * 3)];
      setJsAnswer(chosenAnswer);
      if (chosenAnswer === currentQuestion.correct_answer) {
        setJsPoints(jsPoints + 1);
      }
    }
  };

  // functions to change game state
  const correctAnswer = () => {
    getJsAnswer();
    setGameStatus("correct");
    setHumanPoints(humanPoints + 1);
    setBackground("green");
  };

  const wrongAnswer = () => {
    getJsAnswer();
    setGameStatus("wrong");
    setBackground("red");
  };

  const nextQuestion = () => {
    if (currentQuestionNumber < 10) {
      setJsAnswer("");
      setGameStatus("question");
      setBackground("blue");
      setCurrentQuestionNumber(currentQuestionNumber + 1);
      setCurrentAnswers(getAnswers(questions[currentQuestionNumber]));
    } else if (humanPoints > jsPoints) {
      setGameStatus("win");
      setBackground("yellow");
    } else {
      setGameStatus("defeat");
      setBackground("grey");
    }
  };

  const restart = () => {
    api
      .get("/questions")
      .then(result => setQuestions(result.data))
      .then(() => {
        setGameStatus("init");
        setCurrentQuestionNumber(0);
        setHumanPoints(0);
        setJsPoints(0);
        setBackground("blue");
        setCurrentAnswers([]);
        setJsAnswer("");
      });
  };

  // load in questions at startup
  useEffect(() => {
    api.get("/questions").then(result => setQuestions(result.data));
  }, []);

  return (
    <div id="page" className={background}>
      <section id="quiz" ref={quizRef}>
        <div id="menu">
          <FaCog
            onClick={() => {
              scroll(settingsRef);
            }}
            className="controls"
            style={{ alignItems: "start" }}
            size={"3vw"}
          />
          <FaRedoAlt
            onClick={restart}
            className="controls"
            style={{ alignItems: "start" }}
            size={"3vw"}
          />
        </div>

        <div id="statusBoard">
          <Progress
            style={{ alignItems: "end" }}
            current={currentQuestionNumber}
            finish="10"
          />
          <Score
            style={{ alignItems: "end" }}
            id="score"
            humanPoints={humanPoints}
            jsPoints={jsPoints}
          />
        </div>

        <div>
          <div id="questionContainer">
            {gameStatus === "init" && (
              <div
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center"
                }}
              >
                <div
                  id="startButton"
                  onClick={() => {
                    setGameStatus("question");
                    setCurrentQuestionNumber(currentQuestionNumber + 1);
                    setCurrentAnswers(
                      getAnswers(questions[currentQuestionNumber])
                    );
                  }}
                >
                  START
                </div>
              </div>
            )}

            {gameStatus === "question" && (
              <div style={{ display: "grid", gridTemplateColumns: "100%" }}>
                <div id="question-text">
                  {questions[currentQuestionNumber - 1].question}
                </div>
                <div id="answer-buttons">
                  <QuizButton
                    handleClick={() =>
                      currentAnswers[0] ===
                      questions[currentQuestionNumber - 1].correct_answer
                        ? correctAnswer()
                        : wrongAnswer()
                    }
                    text={currentAnswers[0]}
                  />
                  <QuizButton
                    handleClick={() =>
                      currentAnswers[1] ===
                      questions[currentQuestionNumber - 1].correct_answer
                        ? correctAnswer()
                        : wrongAnswer()
                    }
                    text={currentAnswers[1]}
                  />
                  <QuizButton
                    handleClick={() =>
                      currentAnswers[2] ===
                      questions[currentQuestionNumber - 1].correct_answer
                        ? correctAnswer()
                        : wrongAnswer()
                    }
                    text={currentAnswers[2]}
                  />
                  <QuizButton
                    handleClick={() =>
                      currentAnswers[3] ===
                      questions[currentQuestionNumber - 1].correct_answer
                        ? correctAnswer()
                        : wrongAnswer()
                    }
                    text={currentAnswers[3]}
                  />
                </div>
              </div>
            )}

            {gameStatus === "correct" && (
              <div style={{ display: "grid", gridTemplateColumns: "100%" }}>
                <div className="correct notifier">Your answer was correct!</div>
                <div className="nextButton">
                  <QuizButton handleClick={nextQuestion} text="Next" />
                </div>
                <div style={{ textAlign: "center", fontWeight: "bold" }}>
                  JS has chosen: {jsAnswer}
                </div>
              </div>
            )}

            {gameStatus === "wrong" && (
              <div style={{ display: "grid", gridTemplateColumns: "100%" }}>
                <div className="wrong notifier">
                  Wrong! The correct answer was:
                  <br />
                  {questions[currentQuestionNumber - 1].correct_answer}
                </div>
                <QuizButton handleClick={nextQuestion} text="Next" />
                <div style={{ textAlign: "center", fontWeight: "bold" }}>
                  JS has chosen: {jsAnswer}
                </div>
              </div>
            )}

            {gameStatus === "win" && (
              <div style={{ display: "grid", gridTemplateColumns: "100%" }}>
                <div className="win notifier">You won! Congratulations!</div>
                <QuizButton handleClick={restart} text="Restart" />
              </div>
            )}

            {gameStatus === "defeat" && (
              <div style={{ display: "grid", gridTemplateColumns: "100%" }}>
                <div className="defeat notifier">You lost. Maybe next time!</div>
                <QuizButton handleClick={restart} text="Restart" />
              </div>
            )}
          </div>
        </div>
      </section>

      <section id="settings" ref={settingsRef}>
        <FaArrowUp
          className="controls"
          onClick={() => {
            scroll(quizRef);
          }}
          size={"3vw"}
        />
        <div style={{ marginTop: "3vw" }}>
          <QuestionEdit />
        </div>
      </section>
    </div>
  );
};

export default App;