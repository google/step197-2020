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

  nextQuizWord() {
    while (this.currentArray < this.quiz.length &&
      this.quiz[this.currentArray].length == 0) {
      this.currentArray++;
    }
    if (this.currentArray == this.quiz.length) {
      return null;
    }
    this.currentQuizWord = this.quiz[this.currentArray].shift();
    return this.currentQuizWord;
  }

  updateWordQueues(correct) {
    if (correct === "false" && this.currentArray < this.quiz.length - 1) {
      this.quiz[this.currentArray + 1].push(this.currentQuizWord);
    }
    //TODO(esaaracay): Fetch /study to update familarity score
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
    const words = fetch(`/study?folderKey=${folderKey}`)
      .then((result) => result.json())
      .catch(alert("Could not find words for Study Mode"));
    return words;
  }
}

class MockStudyService {
  constructor(words) {
    this.words = words;
  }
  
  getWordsFromFolder(folderKey) {
    return this.words;
  }

}

export { Quiz, StudyService, MockStudyService };