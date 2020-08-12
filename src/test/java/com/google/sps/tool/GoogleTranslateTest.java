package com.google.sps.tool;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.sps.tool.GoogleTranslate;

public class GoogleTranslateTest {

  @Test
  public void translateHelloToSpanish() {
    assertEquals("Hola", GoogleTranslate.translateText("hello", "es"));
  }

  @Test
  public void translateHelloToVietnamese() {
    assertEquals("xin chào", GoogleTranslate.translateText("hello", "vi"));
  }

  @Test
  public void translateHelloToChineseTraditional() {
    assertEquals("你好", GoogleTranslate.translateText("hello", "zh-CN"));
  }

  @Test
  public void translateHelloToJapanese() {
    assertEquals("こんにちは", GoogleTranslate.translateText("hello", "ja"));
  }
}