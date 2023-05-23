package org.memoryextension.nngm.web;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.memoryextension.nngm.core.RhymingPattern;
import org.springframework.web.client.RestTemplate;


import java.util.*;


@Controller
public class WelcomeController {

    @Autowired
    Environment env;

  @GetMapping("/")
  public String greetingForm(Model model) {
	model.addAttribute("pattern", new RhymingPattern());
    return "form";
  }

   @PostMapping("/generate")
   public String generator(@ModelAttribute RhymingPattern pattern, Model model) {
//      TODO add custom validator, when have time

       if(!patternValid(pattern)){
          model.addAttribute("pattern", pattern);
          return "form";
      }

       List<PoemLineResult> lineResults = new ArrayList<>();
       List<String> generatedLines = generatePoem(pattern);
       for(String line:generatedLines){
           PoemLineResult newLine = new PoemLineResult();
           newLine.setLine(line);
           lineResults.add(newLine);
       }

       model.addAttribute("poemLines", lineResults);
     model.addAttribute("pattern", pattern);
     return "result";
   }

   private Boolean patternValid(RhymingPattern rhymingPattern){
       String[] patterns = rhymingPattern.getPatterns().trim().split("\\s+");
       for(String pattern: patterns){
           if(pattern != pattern.toUpperCase()) return false;
           if (pattern.length() == 2 || pattern.length() == 4 || pattern.length() == 6){
               Map<Character, Integer> wordsCount = new HashMap<>();
               for(Character c: pattern.toUpperCase().toCharArray()){
                   wordsCount.put(c, wordsCount.getOrDefault(c, 0)+1);
               }
               for(Integer count: wordsCount.values()){
                   if (count != 2) return false;
               }
           }else{
               return false;
           }
       }
      return true;
   }

   private List<String> generatePoem(RhymingPattern rhymingPattern){
      Map<Character, String[]> poemPattern = new Hashtable<>();
      List<String> poemFinal = new ArrayList<String>();

      int numOfRyhmeUsed = 0;
      for(Character c: rhymingPattern.getPatterns().toCharArray()){
          if (c == ' '){
              poemFinal.add("");
              continue;
          }
          String[] twelveSyllables = generateTwelveSyllables();
          String[] ryhmeReturn = getWordFromRhyme(twelveSyllables[twelveSyllables.length - 1]);
          if(!poemPattern.containsKey(c)){
              poemPattern.put(c,ryhmeReturn);
              poemFinal.add(String.join(" ", twelveSyllables));
          }else{
              String[] temp =  Arrays.copyOfRange(twelveSyllables, 0, 10);
              String[] currentRyhme = poemPattern.get(c);
              String currentPoemLine = String.join(" ", temp) + " " + currentRyhme[numOfRyhmeUsed];
              poemFinal.add(currentPoemLine);
              numOfRyhmeUsed += 1;
          }
      }
      return poemFinal;

   }

   private String[] generateTwelveSyllables(){
       int numOfSyllables = 0;
       int nextNumOfSyllables = 0;
       List<String> result = new ArrayList<String>();

      while (numOfSyllables != 12){
          String[] markovString = getTextFromMarkov();

          for(String word: markovString){
              nextNumOfSyllables = countSyllableWord(word);

              if ((numOfSyllables + nextNumOfSyllables) > 12 || nextNumOfSyllables > 3){
                  continue;
              }


              numOfSyllables = numOfSyllables + nextNumOfSyllables;

              if (numOfSyllables == 12){
                  String[] ryhmeReturn = getWordFromRhyme(word);
                  if (ryhmeReturn.length < 10){
                      numOfSyllables = numOfSyllables - nextNumOfSyllables ;
                      continue;
                  }else{
                      result.add(word);
                      break;
                  }
              }
              result.add(word);

          }
      }

       String[] arr = new String[result.size()];
       arr = result.toArray(arr);
      return arr;
   }
   private String[] getTextFromMarkov(){
       String markovServer = env.getProperty("markov.server.url");
       RestTemplate restTemplate = new RestTemplate();
       ResponseEntity<String[]> response
               = restTemplate.getForEntity(markovServer, String[].class);
       return response.getBody();
   }

   private Integer countSyllableWord(String someWord){
       String syllableCountWord = env.getProperty("syllable.server.countWord.url");
       RestTemplate restTemplate = new RestTemplate();
       ResponseEntity<Integer> response
               = restTemplate.getForEntity(syllableCountWord+someWord, Integer.class);
       return response.getBody();
   }

    private Integer countSyllableSentence(String[] someWords){
        String syllableCountSentence = env.getProperty("syllable.server.countSentence.url");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String[]> request = new HttpEntity<>(someWords);
        ResponseEntity<Integer> response
                = restTemplate.postForEntity(syllableCountSentence,request, Integer.class);
        return response.getBody();
    }

    private String[] getWordFromRhyme(String someWord){
        String rhymeServer = env.getProperty("rhyme.server.url");
        RestTemplate restTemplate = new RestTemplate();
        String rhymeJsonString = restTemplate.getForObject(rhymeServer+someWord, String.class);
        ObjectMapper mapper = new ObjectMapper();

        List<String> returnList = new ArrayList<String>();

        try {
            JsonNode root = mapper.readTree(rhymeJsonString);
            JsonNode strictNode = root.findPath("strict");
            JsonNode oneNode = root.findPath("one");
            JsonNode twoNode = root.findPath("two");
            JsonNode threeNode = root.findPath("three");
            if (strictNode.isArray()) {
                for (JsonNode rhyme : strictNode) {
                    returnList.add(rhyme.asText());
                }
            }
            if (oneNode.isArray()) {
                for (JsonNode rhyme : oneNode) {
                    returnList.add(rhyme.asText());
                }
            }
            if (twoNode.isArray()) {
                for (JsonNode rhyme : twoNode) {
                    returnList.add(rhyme.asText());
                }
            }
            if (threeNode.isArray()) {
                for (JsonNode rhyme : threeNode) {
                    returnList.add(rhyme.asText());
                }
            }

        }catch(com.fasterxml.jackson.core.JsonProcessingException e){

        }
        String[] arr = new String[returnList.size()];
        arr = returnList.toArray(arr);
        return arr;

//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Hashtable<String, List<String>>> response
//                = restTemplate.getForObject().getForEntity(rhymeServer+someWord, Hashtable<String, List<String>>.class);
//        return response.getBody();

    }
//    @ExceptionHandler(ConstraintViolationException.class)
//    String handleConstraintViolationException(ConstraintViolationException e) {
//        return "form";
//    }

}