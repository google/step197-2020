let Quiz;
let currentQuizWord = {};
let currentArray = 0;
let test = 0;

async function startQuiz(folderKey) {
await fetch(`/study?folderKey=${folderKey}`)
     .then(res => res.json())
     .then(result => {
         Quiz = result;
         console.log(Quiz[0].length);
         return Quiz.length;
    }).catch("Could not find any cards");
}

function nextQuizWord() {
    console.log(Quiz);
    console.log(test);
  let nextWord = {};
  if (Quiz[currentArray].length != 0) {
    nextWord = Quiz[currentArray].shift();
  } else {
    currentArray++;
    if (currentArray >= Quiz.length) {
      return "!@end";
    } else {
      nextWord = Quiz[currentArray].shift();
    }
  }
  currentQuizWord = nextWord;
  return nextWord;
}

function updateWordQueues(correct) {
  if (correct == "false" && currentArray < Quiz.length - 1) {
    Quiz[currentArray + 1].push(currentQuizWord);
  }
  //TODO(esaaracay): Fetch /study to update familarity score
}

function getRound() {
  return currentArray;
}

export { startQuiz, updateWordQueues, nextQuizWord, getRound };