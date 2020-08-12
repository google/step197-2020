import privateInfo from "../../../../../credentials/token.json";

/**
 * Fetches translated text using the Google Translate API
 */
export function getTranslation(term, fromLang, toLang) {
  const response = fetch("/translation", {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      rawText: `${term}`,
      toLang: `${toLang}`,
      fromLang: `${fromLang}`,
    }),
  })
    .then((res) => res.json())
    .then((result) => {
      return result.translation;
    })
    .catch((error) => {
      return "Could not Translate";
    });
}
