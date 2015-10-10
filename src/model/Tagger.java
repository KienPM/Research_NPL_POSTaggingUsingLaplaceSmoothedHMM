/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import utils.Util;

/**
 *
 * @author Ken
 */
public class Tagger {

    // stores the tags

    private String[] tags;
    // map tag and the order of appearance in tags array
    private HashMap<String, Integer> map;
    // pTransition[i][0] stores the probability tag[i] appears in begin of a senctence
    // pTransition[i][j] stores the probability tag[i] appears after tag[j] 
    private double[][] pTransition = new double[Constant.NUMBER_OF_TAGS + 1][Constant.NUMBER_OF_TAGS + 1];
    // stores individually words, each word mathes with a probabilities array
    // probabilities[i] (with i >= 1) is the probability this word emitted in tag i
    private HashMap<String, double[]> pEmit;

    public Tagger() throws FileNotFoundException {
        tags = Util.loadPennTreebank();
        mapTagWithOrder();
    }

    private void mapTagWithOrder() {
        for (int i = 0; i < tags.length; ++i) {
            map.put(tags[i], i + 1);
        }
    }

    private void loadPTransition() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(Constant.PTRANSITION_PATH));
        int k = Constant.NUMBER_OF_TAGS;
        for (int i = 1; i <= k; ++i) {
            for (int j = 0; j <= k; ++j) {
                pTransition[i][j] = scanner.nextDouble();
            }
        }
    }

    private void loadPEmit() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(Constant.PEMIT_PATH));
        int k = Constant.NUMBER_OF_TAGS;
        long n = scanner.nextLong();
        for (long i = 0; i <= n; ++i) {
            String s = scanner.nextLine();
            String[] tokens = s.trim().split("[\\s]+");
            double[] p = new double[k + 1];
            for (int j = 2; j <= k; ++ j) {
                p[j - 1] = Double.parseDouble(tokens[j]);
            }
            pEmit.put(tokens[0], p);
        }
    }
}
