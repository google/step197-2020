package com.google.sps.tool;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.TranslateOptions.Builder;
import com.google.cloud.translate.Translation;

public class GoogleTranslate {

  public static String translateText(String textNotTranslated, String targetLanguage) {

    Translate translate = TranslateOptions.newBuilder().setProjectId("framecards").setQuotaProjectId("framecards").build().getService();
    Translation translation = 
        translate.translate(textNotTranslated, Translate.TranslateOption.targetLanguage(targetLanguage));
    String translatedText = translation.getTranslatedText();

    return translatedText;
  }

}