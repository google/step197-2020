import React from 'react';
import Flashcard from "./Flashcard.js";

/* Sample flashcard data */
const flashcardData=[
    {
        labels: "11037",
        imageBlobKey:"",
        text:"dog",
        translation:"perro",
    },
    {
        labels: "11037",
        imageBlobKey:"",
        text:"cat",
        translation:"gato",
    },
    {
        labels: "11037",
        imageBlobKey:"",
        text:"hello",
        translation:"hola",
    },
    {
        labels: "11037",
        imageBlobKey:"",
        text:"bear",
        translation:"oso",
    },
];
const containerStyle={
    display:"flex",
    alignItems: "center",
    justifyContent: "space-around",
    flexFlow: "row wrap",
}
/*
    Holds a container of flashcards.
    TODO: Integrate backend to retrieve flashcard data
*/
class FlashcardContainer extends React.Component{
    getFlashcards(){
        
    }
    render(){ 
        const flashcardItems = flashcardData.map((flashcard) =>
            <Flashcard 
                key={flashcard.text} 
                image={flashcard.imageBlobKey}
                text={flashcard.text} 
                translation={flashcard.translation}
                labels={flashcard.labels} />
        );
        return(
            <div className="FlashcardContainer" style={containerStyle}>
                {flashcardItems}
            </div>
        );
    }    
}

export default FlashcardContainer;