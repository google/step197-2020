class Quiz {
  constructor(studyService) {
    this.quiz = {};
    this.currentArray = 0;
    this.currentQuizWord = {};
    this.studyService = studyService;
  }

  async start(folderKey) {
    const quiz = await this.studyService.getWordsFromFolder(folderKey);
    this.quiz = quiz;
    this.currentArray = 0;
  }

  /**
   * Checks if the current array has any cards left to return and if
   * not then it moves onto the next array. If there are no more
   * arrays left then the function returns null.
   */
  nextQuizWord() {
    while (
      this.currentArray < this.quiz.length &&
      this.quiz[this.currentArray].length == 0
    ) {
      this.currentArray++;
    }
    if (this.currentArray == this.quiz.length) {
      return null;
    }
    this.currentQuizWord = this.quiz[this.currentArray].shift();
    return this.currentQuizWord;
  }

  async updateWordQueues(correct, cardKey) {
    if (correct === "false" && this.currentArray < this.quiz.length - 1) {
      this.quiz[this.currentArray + 1].push(this.currentQuizWord);
    }
    // Updates the card's familarity score and stores it
    const updated = await fetch("/study", {
      method: "POST",
      body: `cardKey=${cardKey}&answeredCorrectly=${correct}`,
      headers: { "Content-type": "application/x-www-form-urlencoded" },
    });

    return updated;
  }

  getCurrentRound() {
    return this.currentArray + 1;
  }

  getTotalRounds() {
    return this.quiz.length;
  }
}

class StudyService {
  async getWordsFromFolder(folderKey) {
    const words = await fetch(`/study?folderKey=${folderKey}`)
      .then((result) => result.json())
      .catch((error) => {
        alert("No cards were found");
        window.location = "/StudyMode"
      });
    return words;
  }
}

export { Quiz, StudyService };