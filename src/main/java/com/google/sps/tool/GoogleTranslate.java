package com.google.sps.tool;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class GoogleTranslate {

  private static final Translate TRANSLATE_SERVICE =
      TranslateOptions.newBuilder()
          .setProjectId("framecards")
          .setQuotaProjectId("framecards")
          .build()
          .getService();

  public static String translateText(String originalText, String targetLanguage) {
    Translation translation =
        TRANSLATE_SERVICE.translate(
            originalText, Translate.TranslateOption.targetLanguage(targetLanguage));
    String translatedText = translation.getTranslatedText();

    return translatedText;
  }
}
