/**
 * Fetches translated text using the Google Translate API
 */
async function getTranslation(term, fromLang, toLang) {
  const searchParams = new URLSearchParams();
  searchParams.append("rawText", term);
  searchParams.append("toLang", toLang);
  searchParams.append("fromLang", fromLang);
  const response = await fetch(('/translation?'+ searchParams.toString()))
    .then(res => res.json())
    .then((result) => {
      return result;
    })
    .catch((error) => {
      return "Text could not be translated";
    });
  
  return response;
}

export {getTranslation}