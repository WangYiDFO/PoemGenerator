package org.memoryextension.nngm.syllable;

import org.memoryextension.nngm.core.Syllable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class SyllableController {

    @GetMapping("/countWord/{someWord}")
    public ResponseEntity<Integer> countWord(@PathVariable final String someWord) {

       return ResponseEntity.ok(Syllable.count(someWord));

    }

    @PostMapping("/countSentence")
    public ResponseEntity<Integer> countSentence(@RequestBody final String[] sentence) {
        int totalCount = 0;
        for(String word: sentence){
            totalCount += Syllable.count(word);
        }
        return ResponseEntity.ok(totalCount);
    }

}