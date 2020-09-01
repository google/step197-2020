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

  updateWordQueues(correct) {
    if (correct === "false" && this.currentIndex < this.quiz.length - 1) {
      this.quiz[this.currentIndex + 1].push(this.currentQuizWord);
    }
    //TODO(esaaracay): Fetch /study to update familarity score
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
    const words = fetch(`/study?folderKey=${folderKey}`)
      .then((result) => result.json())
      .catch(alert("Could not find words for Study Mode"));
    return words;
  }
}

export { Quiz, StudyService };