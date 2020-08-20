let Quiz = [];
let currentQuizWord = {};
let currentArray = 0;

async function startQuiz(folderKey) {
    //TODO(esaracay): Fetch /study servelet and parse json into object
    Quiz = [
        [{ cardKey: 1, quizWord: "hola", possibleResponses: ["hinder", "hello", "hone", "help"], correctAnswer: "hello" }],
        [{ cardKey: 2, quizWord: "hola2", possibleResponses: ["hinder", "hello", "hone", "help"], correctAnswer: "hello" }],
        [{ cardKey: 3, quizWord: "hola3", possibleResponses: ["hinder", "hello", "hone", "help"], correctAnswer: "hello" }]
    ];
    return 3;
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

function updateWordQueues(correct) {
    if (correct == "false" && currentArray < (Quiz.length - 1)) {
        Quiz[currentArray + 1].push(currentQuizWord);
    }
    //TODO(esaaracay): Fetch /study to update familarity score
}

function getRound() {
    return currentArray;
}

export { startQuiz, updateWordQueues, nextQuizWord, getRound };