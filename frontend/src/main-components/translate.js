
export function getTranslation(term, lang) {
    const response = fetch(
      "https://translation.googleapis.com/language/translate/v2",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-Goog-User-Project": "framecards",
          Authorization: `Bearer $(gcloud auth application-default print-access-token)`,
        },
        redirect: "follow",
        referrer: "no-referrer",
        body: JSON.stringify({
          q: term,
          target: lang,
        })
      })
      .then((res) => res.json())
      .then((result) => {
        console.log(response);
      });
}

