let quiz = [];
let currentQuizWord = {};
let currentArray = 0;

function startQuiz(folderKey) {
    fetch(`/study?folderKey=${folderKey}`, { method: 'POST' })
        .then(result => result.json())
        .then(arrays => {
            quiz = arrays;
            return arrays.length;
        })
        .catch(alert("Could not find words for Study Mode"));
}

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

function updateWordQueues(correct) {
    if (correct === "false" && currentArray < (quiz.length - 1)) {
        quiz[currentArray + 1].push(currentQuizWord);
    }
    //TODO(esaaracay): Fetch /study to update familarity score
}

function getRound() {
    return currentArray;
}

export { startquiz, updateWordQueues, nextquizWord, getRound };