let Quiz = [];
let currentQuizWord = {};
let currentArray = 0;

function startQuiz(folderKey) {
    fetch(`/study?folderKey=${folderKey}`, { method: 'POST' })
        .then(result => result.json())
        .then(arrays => {
            Quiz = arrays;
            return arrays.length;
        })
        .catch(alert("Could not find words for Study Mode"));
}

function nextQuizWord() {
    let nextWord = {};
    if (Quiz[currentArray].length != 0) {
        nextWord = Quiz[currentArray].shift();
    } else {
        currentArray++;
        if (currentArray >= Quiz.length) {
            return null;
        } else {
            nextWord = Quiz[currentArray].shift();
        }
    }
    currentQuizWord = nextWord;
    return nextWord;
}

function updateWordQueues(correct) {
    if (correct === "false" && currentArray < (Quiz.length - 1)) {
        Quiz[currentArray + 1].push(currentQuizWord);
    }
    //TODO(esaaracay): Fetch /study to update familarity score
}

function getRound() {
    return currentArray;
}

export { startQuiz, updateWordQueues, nextQuizWord, getRound };