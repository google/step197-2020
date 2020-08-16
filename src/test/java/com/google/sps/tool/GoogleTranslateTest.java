package com.google.sps.tool;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class GoogleTranslateTest {

  @Test
  public void translateHelloToSpanish() {
    assertNotNull(GoogleTranslate.translateText("hello", "es"));
  }

  @Test
  public void translateHelloToVietnamese() {
    assertNotNull(GoogleTranslate.translateText("hello", "vi"));
  }

  @Test
  public void translateHelloToChineseTraditional() {
    assertNotNull(GoogleTranslate.translateText("hello", "zh-CN"));
  }

  @Test
  public void translateHelloToJapanese() {
    assertNotNull(GoogleTranslate.translateText("hello", "ja"));
  }
}
