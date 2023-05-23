package org.memoryextension.nngm.rhyme;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ca.rmen.rhymer.RhymeResult;
import ca.rmen.rhymer.Rhymer;
import ca.rmen.rhymer.cmu.CmuDictionary;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;


@RestController
public class RhymeController {

    @GetMapping("/getRhyme/{someWord}")
    public ResponseEntity<Hashtable> getRhyme(@PathVariable final String someWord) {

        try {
            Rhymer rhymer = CmuDictionary.loadRhymer();
            List<RhymeResult> results = rhymer.getRhymingWords(someWord);

            Hashtable returnResult = new Hashtable();
            for (RhymeResult result : results) {
                returnResult.put("strict",result.strictRhymes);
                returnResult.put("one",result.oneSyllableRhymes);
                returnResult.put("two",result.twoSyllableRhymes);
                returnResult.put("three",result.threeSyllableRhymes);

            }
            if(returnResult.isEmpty()){
                return new ResponseEntity("[]", HttpStatus.OK);
            }else{
                return ResponseEntity.ok(returnResult);
            }

        }
        catch (IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}