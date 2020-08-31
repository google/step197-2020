import React from "react";
import CreateFolderContent from "../main-components/CreateFolderContent";
import { Quiz } from "../main-components/StudyModeGameHandler";

describe("Testing Quiz class", () => {
  class MockStudyService {
    constructor(words) {
      this.words = words;
    }

    getWordsFromFolder(folderKey) {
      return this.words;
    }
  }

  const testObject = [
    [
      {
        quizWord: "dog",
        cardKey: 1,
        possibleResponses: ["dog", "dodge", "does", "do"],
        correctAnswer: "dog",
      },
    ],
    [
      {
        quizWord: "cat",
        cardKey: 2,
        possibleResponses: ["cast", "catch", "cat", "cash"],
        correctAnswer: "cat",
      },
    ],
  ];
  
  const mockService = new MockStudyService(testObject);
  const quiz = new Quiz(mockService);
  quiz.start(1);

  test("Returns correct number of rounds", () => {
    expect(quiz.getTotalRounds()).toEqual(2);
  });

  test("Returns first Word", () => {
    const firsWord = quiz.nextQuizWord();
    expect(firsWord.quizWord).toEqual("dog");
  });

  test("Grabs Second Word", () => {
    const secondWord = quiz.nextQuizWord();
    expect(secondWord.quizWord).toEqual("cat");
  });

  test("Returns current Round", () => {
    const currentRound = quiz.getCurrentRound();
    expect(currentRound).toEqual(2);
  });

  test("Returns null for last word", () => {
    const final = quiz.nextQuizWord();
    expect(final).toEqual(null);
  });
});