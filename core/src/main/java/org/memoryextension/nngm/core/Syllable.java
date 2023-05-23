package org.memoryextension.nngm.core;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Syllable {
  
  public static int count(String word){

    // inspired by https://www.syllablecount.com/syllable/rules/

    int counter = 0;
    word = word.toLowerCase(); // converting all string to lowercase
    if(word.contains("the ")){
      counter++;
    }
    String[] split = word.split("e!$|e[?]$|e,|e |e[),]|e$");

    ArrayList<String> al = new ArrayList<String>();
    Pattern tokSplitter = Pattern.compile("[aeiouy]+");

    for (int i = 0; i < split.length; i++) {
      String s1 = split[i];
      Matcher m = tokSplitter.matcher(s1);

      while (m.find()) {
        al.add(m.group());
      }
    }

    counter += al.size();
    return counter;
  }
  
}