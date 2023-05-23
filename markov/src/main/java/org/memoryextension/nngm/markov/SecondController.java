package org.memoryextension.nngm.markov;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SecondController {

  @GetMapping("/second")
  public String greetingForm(@RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {
	model.addAttribute("name", name);
    return "hello";
  }


}