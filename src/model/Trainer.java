/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Util;
import vn.hus.nlp.utils.UTF8FileUtility;

/**
 *
 * @author Ken
 */
public class Trainer {

    // stores the tags

    private String[] tags;
    // map tag and the order of appearance in tags array
    private HashMap<String, Integer> map;
    // cTags[i] stores the frequency of tag[i]
    private long[] cTags = new long[Constant.NUMBER_OF_TAGS];
    // cTransform[i][0] stores the frequency tag[i] appears in begin of a senctence
    // cTransform[i][j] stores the frequency tag[i] appears before tag[j]
    private long[][] cTransform = new long[Constant.NUMBER_OF_TAGS + 1][Constant.NUMBER_OF_TAGS + 1];
    // pTransform[i][0] stores the probability tag[i] appears in begin of a senctence
    // pTransform[i][j] stores the probability tag[i] appears after tag[j] 
    private double[][] pTransform = new double[Constant.NUMBER_OF_TAGS + 1][Constant.NUMBER_OF_TAGS + 1];

    public Trainer() throws FileNotFoundException {
        tags = Util.loadPennTreebank();
        map = new HashMap<>();
        mapTagAndOrder();
    }

    private void mapTagAndOrder() {
        for (int i = 0; i < tags.length; ++i) {
            map.put(tags[i], i + 1);
        }
    }

    public void analyzeTrainingData(File[] dataFolders) {
        for (int i = 0; i < dataFolders.length; ++i) {
            File[] files = dataFolders[i].listFiles();
            for (int k = 0; k < files.length; ++k) {
                String[] lines = UTF8FileUtility.getLines(files[k].getAbsolutePath());
                String content = "";
                for (int j = 0; j < lines.length; ++j) {
                    content += lines[j] + " ";
                }
                content = Util.toTaggedForm(content);
//                System.out.println(content);
                String[] taggedWords = content.split(" ");
                int index;
                String word, tag, tagBefore = "";
                for (int j = 0; j < taggedWords.length; ++j) {
                    if (taggedWords[j].matches("=")) {
                        ++j;
                        index = taggedWords[j].indexOf("/");
                        word = taggedWords[j].substring(0, index);
                        tag = taggedWords[j].substring(index + 1);
                        int order = map.get(tag);
                        ++cTransform[order][0];
                        ++cTags[order];
                        tagBefore = tag;
                        continue;
                    }
                    index = taggedWords[j].indexOf("/");
                    word = taggedWords[j].substring(0, index);
                    tag = taggedWords[j].substring(index + 1);
                    
                }
            }
        }
    }

    public static void main(String[] args) {
        File f = new File("G:\\Training data\\POS tagging\\wsj\\functional test");
        File[] files = new File[]{f};
        try {
            new Trainer().analyzeTrainingData(files);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Trainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
