let quiz = [];
let currentQuizWord = {};
let currentArray;

async function startQuiz(folderKey) {
  currentArray = 0;
  const numRounds = await fetch(`/study?folderKey=${folderKey}`)
     .then(res => res.json())
     .then(result => {
         quiz = result;
         return quiz.length;
     }).catch("Could not find any cards");
  return numRounds;
}

/**
 * Checks if the current array has any cards left to return and if
 * not then it moves onto the next array. If there are no more
 * arrays left then the function returns null.
 */
function nextQuizWord() {
    let nextWord = {};
    if (quiz[currentArray].length != 0) {
        nextWord = quiz[currentArray].shift();
    } else {
        currentArray++;
        if (currentArray >= quiz.length) {
            return null;
        } else {
            nextWord = quiz[currentArray].shift();
        }
    }
  currentQuizWord = nextWord;
  return nextWord;
}

function updateWordQueues(correct, cardKey) {
  if (correct == "false" && currentArray < Quiz.length - 1) {
    quiz[currentArray + 1].push(currentQuizWord);
  }
  // Updates the card's familarity score and stores it
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