/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.StringTokenizer;
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
    private long[] cTags = new long[Constant.NUMBER_OF_TAGS + 1];
    // cTransform[i][0] stores the frequency tag[i] appears in begin of a senctence
    // cTransform[i][j] stores the frequency tag[i] appears before tag[j]
    private long[][] cTransition = new long[Constant.NUMBER_OF_TAGS + 1][Constant.NUMBER_OF_TAGS + 1];
    // pTransition[i][0] stores the probability tag[i] appears in begin of a senctence
    // pTransition[i][j] stores the probability tag[i] appears after tag[j] 
    private double[][] pTransition = new double[Constant.NUMBER_OF_TAGS + 1][Constant.NUMBER_OF_TAGS + 1];

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
                String[] taggedWords = content.trim().split("[\\s]+");
                int index;
                String word, tag, tagBefore = "";
                index = taggedWords[0].indexOf("/");
                word = taggedWords[0].substring(0, index);
                tag = taggedWords[0].substring(index + 1);
                int order = map.get(tag);
                ++cTransition[order][0];
                ++cTags[order];
                tagBefore = tag;
                int n = taggedWords.length;
                for (int j = 1; j < n; ++j) {
                    if (taggedWords[j].equals("./.") && j < n - 1) {
                        ++j;
                        index = taggedWords[j].indexOf("/");
                        word = taggedWords[j].substring(0, index);
                        tag = taggedWords[j].substring(index + 1);
                        order = map.get(tag);
                        ++cTransition[order][0];
                        ++cTags[order];
                        tagBefore = tag;
                        continue;
                    }
                    index = taggedWords[j].indexOf("/");
                    word = taggedWords[j].substring(0, index);
                    tag = taggedWords[j].substring(index + 1);
                    order = map.get(tag);
                    ++cTags[order];
                    ++cTransition[map.get(tagBefore)][order];
                    tagBefore = tag;
                }
            }
        }
    }
    
    public void calculatePTransition() {
        int k = Constant.NUMBER_OF_TAGS;
        
        // Calculate probability of the sequence starting in tag[i]
        long n = Util.sumColumn(cTransition, 0);
        for (int i = 1; i <= k; ++i) {
            pTransition[0][i] = (double) (cTransition[0][i] + 1) / (n + k);
        }
        
        // Calculate probability of the sequence transitioning from tag[i] to tag[j]
        for (int i = 1; i <= k; ++i) {
            for (int j = 1; j <= k; ++j) {
                pTransition[i][j] = (double) (cTransition[i][j] + 1) / (cTags[i] + k);
            }
        }
    }

    public static void main(String[] args) {
        File f = new File("G:\\Training data\\POS tagging\\wsj\\functional test");
        File[] files = new File[]{f};
        try {
            Trainer trainer = new Trainer();
            trainer.analyzeTrainingData(files);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Trainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
