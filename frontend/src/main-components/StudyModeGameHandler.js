class Quiz {
  constructor(studyService) {
    this.quiz = {};
    this.currentIndex = 0;
    this.currentQuizWord = {};
    this.studyService = studyService;
  }

  async start(folderKey) {
    const quiz = await this.studyService.getWordsFromFolder(folderKey);
    this.quiz = quiz;
    this.currentIndex = 0;
  }

  /**
   * Checks if the current array has any cards left to return and if
   * not then it moves onto the next array. If there are no more
   * arrays left then the function returns null.
   */
  nextQuizWord() {

    while (this.currentIndex < this.quiz.length &&
      this.quiz[this.currentIndex].length == 0) {
      this.currentIndex++;
    }
    if (this.currentIndex == this.quiz.length) {
      return null;
    }
    this.currentQuizWord = this.quiz[this.currentIndex].shift();
    return this.currentQuizWord;
  }


  async updateWordQueues(correct, cardKey) {
    if (correct === "false" && this.currentIndex < this.quiz.length - 1) {
      this.quiz[this.currentIndex + 1].push(this.currentQuizWord);
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
    return this.currentIndex + 1;
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