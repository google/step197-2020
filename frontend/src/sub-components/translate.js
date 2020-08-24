/**
 * Fetches translated text using the Google Translate API
 */
async function getTranslation(term, fromLang, toLang) {
  const response = await fetch(`/translation?rawText=${term}&toLang=${toLang}&fromLang=${fromLang}`)
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