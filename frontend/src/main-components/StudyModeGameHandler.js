let Quiz;
let currentQuizWord = {};
let currentArray;

async function startQuiz(folderKey) {
  currentArray = 0;
  await fetch(`/study?folderKey=${folderKey}`)
     .then(res => res.json())
     .then(result => {
         Quiz = result;
         return Quiz.length;
    }).catch("Could not find any cards");
}

function nextQuizWord() {
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

function updateWordQueues(correct, cardKey) {
  if (correct == "false" && currentArray < Quiz.length - 1) {
    Quiz[currentArray + 1].push(currentQuizWord);
  }
  // Updates the card's familarity score 
  fetch('/study', {
    method: 'POST',
    body: `cardKey=${cardKey}&answeredCorrectly=${correct}`,
    headers: { 'Content-type': 'application/x-www-form-urlencoded' }
  });
}

function getRound() {
  return currentArray;
}

export { startQuiz, updateWordQueues, nextQuizWord, getRound };