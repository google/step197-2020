package com.google.sps.tool;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import com.google.sps.tool.GoogleTranslate;

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