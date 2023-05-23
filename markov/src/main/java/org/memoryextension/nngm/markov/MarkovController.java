package org.memoryextension.nngm.markov;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MarkovController {
  

  @GetMapping("/")
  public String GenerateSomething() {
    return "hello";
  }

}