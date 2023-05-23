package org.memoryextension.nngm.markov;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Vector;

@Component
public class MarkovHealthIndicator
        implements HealthIndicator {

    @Autowired
    Environment env;

    @Override
    public Health health() {
        int localMarkovSize = localMarkovChainSize();
        if ( localMarkovSize> 0) {
            return Health.up().withDetail("chainSize",localMarkovSize).build();
        }else{
            return Health.status("building").build();
        }

    }

    private Integer localMarkovChainSize() {
        int size = 0;
        String markovDataFile = env.getProperty("MarkovData.Location");
        try {
            File file = new File(markovDataFile);
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(markovDataFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Hashtable<String, Vector<String>> markovchain = (Hashtable<String, Vector<String>>) in.readObject();
                in.close();
                fileIn.close();
                size = markovchain.size();
            }
        }
        catch (Exception e){
            return 0;
        }
        return size;
    }
}
