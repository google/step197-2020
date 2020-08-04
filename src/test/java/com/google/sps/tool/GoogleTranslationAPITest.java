package com.google.sps.tool;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.sps.tool.GoogleTranslationAPI;

@RunWith(JUnit4.class)
public class GoogleTranslationAPITest {

  @Test
  public void translateHelloToSpanish() {
    assertEquals("Hola", GoogleTranslationAPI.translateText("hello", "es"));
  }

  @Test
  public void translateHelloToVietnamese() {
    assertEquals("xin chào", GoogleTranslationAPI.translateText("hello", "vi"));
  }

  @Test
  public void translateHelloToChineseTraditional() {
    assertEquals("你好", GoogleTranslationAPI.translateText("hello", "zh-CN"));
  }

  @Test
  public void translateHelloToJapanese() {
    assertEquals("こんにちは", GoogleTranslationAPI.translateText("hello", "ja"));
  }
}