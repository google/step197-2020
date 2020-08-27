class Quiz {
  constructor() {
    this.quiz = {};
    this.currentArray = 0;
    this.currentQuizWord = {};
  }

  async start(folderKey) {
    const quiz = await fetch(`/study?folderKey=${folderKey}`)
      .then((result) => result.json())
      .catch(alert("Could not find words for Study Mode"));
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
    return this.currentArray;
  }

  getTotalRounds() {
    return this.quiz.length;
  }
}

export { Quiz };