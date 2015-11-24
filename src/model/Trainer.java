/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
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
    // stores individually words, each word mathes with a frequencies array
    // frequencies[0] is the frequency of this word in training data
    // frequencies[i] (with i >= 1) is the frequency this word emitted in tag i
    private HashMap<String, long[]> lexicon;
    // stores individually words, each word mathes with a probabilities array
    // probabilities[i] (with i >= 1) is the probability this word emitted in tag i
    private HashMap<String, double[]> pEmit;
    // stores number of word in training data
    private long wordCount;

    public Trainer() throws FileNotFoundException {
        tags = Util.loadPennTreebank();
        map = new HashMap<>();
        lexicon = new HashMap<>();
        pEmit = new HashMap<>();
        wordCount = 0;
        mapTagWithOrder();
    }

    public void train(File[] trainingFolders) {
        analyzeTrainingData(trainingFolders);
        calculatePTransition();
        calculatePEmit();
        saveModel();
    }

    private void mapTagWithOrder() {
        for (int i = 0; i < tags.length; ++i) {
            map.put(tags[i], i + 1);
        }
    }

    public void addToLexicon(String word, int order) {
        word = word.toLowerCase();
        if (!lexicon.containsKey(word)) {
            HashSet<Integer> temp = new HashSet<>();
            long[] frequencies = new long[Constant.NUMBER_OF_TAGS + 1];
            frequencies[0] = 1;
            frequencies[order] = 1;
            lexicon.put(word, frequencies);
        } else {
            long[] frequencies = lexicon.get(word);
            ++frequencies[order];
            ++frequencies[0];
        }
    }

    public void analyzeTrainingData(File[] dataFolders) {
        // For each folder
        for (int i = 0; i < dataFolders.length; ++i) {
            File[] files = dataFolders[i].listFiles();
            // For each file
            for (int k = 0; k < files.length; ++k) {
                String[] lines = UTF8FileUtility.getLines(files[k].getAbsolutePath());
                String content = "";
                for (int j = 0; j < lines.length; ++j) {
                    content += lines[j] + " ";
                }
                content = Util.toTaggedForm(content);
                String[] taggedWords = content.trim().split("[\\s]+");
                int index, order, nTagsBefore = 0;
                String word, tag;
                String[] tagsBefore = new String[3];

                // Add number of words in a file to wordCount
                int n = taggedWords.length;
                wordCount += n;

                // Process for first word in a file
                index = taggedWords[0].lastIndexOf("/");
                word = taggedWords[0].substring(0, index);
                tag = taggedWords[0].substring(index + 1);
                String[] temp = tag.split("[|]");
                nTagsBefore = temp.length;
                for (int x = 0; x < temp.length; ++x) {
                    order = map.get(temp[x]);
                    ++cTransition[order][0];
                    ++cTags[order];
                    addToLexicon(word, order);
                    tagsBefore[x] = temp[x];
                }

                // Process from second word
                for (int j = 1; j < n; ++j) {
                    // If it a word on begin of a sentence
                    if (taggedWords[j].equals("./.") && j < n - 1) {
                        ++j;
                        index = taggedWords[j].lastIndexOf("/");
                        word = taggedWords[j].substring(0, index);
                        tag = taggedWords[j].substring(index + 1);
                        temp = tag.split("[|]");
                        nTagsBefore = temp.length;
                        for (int x = 0; x < temp.length; ++x) {
                            order = map.get(temp[x]);
                            ++cTransition[order][0];
                            ++cTags[order];
                            addToLexicon(word, order);
                            tagsBefore[x] = temp[x];
                        }
                        continue;
                    }

                    // If not
                    index = taggedWords[j].lastIndexOf("/");
                    word = taggedWords[j].substring(0, index);
                    tag = taggedWords[j].substring(index + 1);
                    temp = tag.split("[|]");
                    for (int x = 0; x < temp.length; ++x) {
                        order = map.get(temp[x]);
                        for (int xx = 0; xx < nTagsBefore; ++xx) {
                            ++cTransition[map.get(tagsBefore[xx])][order];
                        }
                        ++cTags[order];
                        addToLexicon(word, order);
                    }
                    nTagsBefore = temp.length;
                    for (int x = 0; x < nTagsBefore; ++x) {
                        tagsBefore[x] = temp[x];
                    }
                }
            }
        }
    }

    public void calculatePTransition() {
        int k = Constant.NUMBER_OF_TAGS;

        // Calculate probability of the sequence starting in tag[i]
        long n = Util.sumColumn(cTransition, 0);
        for (int i = 1; i <= k; ++i) {
            pTransition[i][0] = (double) (cTransition[i][0] + 1) / (n + k);
        }

        // Calculate probability of the sequence transitioning from tag[i] to tag[j]
        for (int i = 1; i <= k; ++i) {
            for (int j = 1; j <= k; ++j) {
                pTransition[i][j] = (double) (cTransition[i][j] + 1) / (cTags[i] + k);
            }
        }
    }

    public void calculatePEmit() {
        int k = Constant.NUMBER_OF_TAGS;
        for (String key : lexicon.keySet()) {
            long[] f = lexicon.get(key);
            double[] p = new double[k + 1];
            for (int i = 1; i <= k; ++i) {
                p[i] = (double) (f[i] + 1) / (cTags[i] + wordCount);
            }
            pEmit.put(key, p);
        }
    }

    public void saveModel() {
        UTF8FileUtility.createWriter(Constant.PEMIT_PATH);
        UTF8FileUtility.write(String.valueOf(pEmit.size()) + "\n");
        for (String key : pEmit.keySet()) {
            double[] a = pEmit.get(key);
            String s = key;
            for (int i = 0; i < a.length; ++i) {
                s += " " + a[i];
            }
            UTF8FileUtility.write(s + "\n");
        }
        UTF8FileUtility.closeWriter();

        UTF8FileUtility.createWriter(Constant.PTRANSITION_PATH);
        for (int i = 0; i <= Constant.NUMBER_OF_TAGS; ++i) {
            String s = "";
            for (int j = 0; j <= Constant.NUMBER_OF_TAGS; ++j) {
                s += pTransition[i][j] + " ";
            }
            UTF8FileUtility.write(s + "\n");
        }
        UTF8FileUtility.closeWriter();
    }

}
