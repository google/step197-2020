import privateInfo from "../../../../../credentials/token.json";

/**
 * Fetches translated text using the Google translate API
 */
export function getTranslation(term, fromLang, toLang) {

  const response = fetch(
    "https://translation.googleapis.com/language/translate/v2",
    {
      body:
        `{'q':${term}, 'source':${fromLang}, 'target':${toLang}, 'format':'text'}`,
      headers: {
        Authorization:
          `Bearer ${privateInfo.token}`,
        "Content-Type": "application/json",
        "X-Goog-User-Project": "framecards",
      },
      method: "POST",
    }
  )
    .then((res) => res.json())
    .then((result) => {
      return result.data.translations.translatedText;
    });
  
  return response;
}

