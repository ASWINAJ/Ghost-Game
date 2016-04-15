package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    public String getAnyWordStartingWith(String prefix) {
        int mid,f=0,l=words.size();
        if(prefix.equals("")) {
            Random random = new Random();
            int r = random.nextInt(words.size());
            return words.get(r);

        }
        int i=0;
        while(f<=l){
            mid = (f+l)/2;


            if(words.get(mid).startsWith(prefix))
                return words.get(mid);
            else {
                String a = words.get(mid);
                if (a.compareTo(prefix) > 0)
                    l=mid-1;
                else
                    f=mid+1;
            }


        }
        return null;

    }



    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
