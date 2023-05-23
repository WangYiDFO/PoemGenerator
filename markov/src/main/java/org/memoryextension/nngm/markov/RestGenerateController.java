package org.memoryextension.nngm.markov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.memoryextension.nngm.core.Markov;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class RestGenerateController {


    @Autowired
    Environment env;

    
    @GetMapping("/generate")
    public ResponseEntity<Vector<String>> generate() {

        String coreDataFile = env.getProperty("CoreData.Location");
        String markovDataFile = env.getProperty("MarkovData.Location");

        Markov markov = new Markov();

        try {
            Hashtable<String, Vector<String>> markovchain = null;

            if (ExistLocalMarkovChain(markovDataFile)){
                // load markov from local file
                FileInputStream fileIn = new FileInputStream(markovDataFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                markovchain = (Hashtable<String, Vector<String>>) in.readObject();
                in.close();
                fileIn.close();

                markov.settMarkovChain(markovchain);

                return ResponseEntity.ok(markov.generateSentence());

            }else{
                //load from core/data

                String[] contents = LoadCoreData(coreDataFile);
                for(String content : contents){
                    markov.addWords(content);
                }

                //add to local disk
                markovchain = markov.getMarkovChain();

                FileOutputStream fileOut = new FileOutputStream(markovDataFile);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(markovchain);
                out.close();
                fileOut.close();

                return ResponseEntity.ok(markov.generateSentence());

            }
           
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private String[] LoadCoreData(String coreDataLocation) throws IOException{
        
        List<String> strList = new ArrayList<>();

        File folder = new File(coreDataLocation);
        if (folder.exists() && folder.isDirectory() ){
            for(File file: folder.listFiles()){
                String strFile = Files.readString(file.toPath());
                strList.add(cleanTextContent(strFile));
            }
        }
        
        return strList.toArray(new String[0]);
    }

    private boolean ExistLocalMarkovChain(String markovDataPath){
        File file = new File(markovDataPath);
        if (file.exists()){
            return true;
        }else{
            return false;
        }
    }

    private static String cleanTextContent(String text)
    {
        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", " ");

        // this Markov implementation use . ? ! as end of sentence. So,
        // strips off all non-ASCII characters
        //text = text.replaceAll("[^\\x00-\\x7F]", " ");
        text = text.replaceAll("[^A-Za-z\\.\\?\\!]", " ");

        // replace multiple spaces with single space
        text = text.replaceAll( "\\s+", " ");

        return text.trim();
    }
    
}