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
    private double[][] pTransition;
    // stores individually words, each word mathes with a probabilities array
    // probabilities[i] (with i >= 1) is the probability this word emitted in tag i
    private HashMap<String, double[]> pEmit;

    public Tagger() throws FileNotFoundException {
        tags = Util.loadPennTreebank();
        map = new HashMap<>();
        mapTagWithOrder();
        pEmit = new HashMap<>();
        pTransition = new double[Constant.NUMBER_OF_TAGS + 1][Constant.NUMBER_OF_TAGS + 1];
        loadModel();
    }

    private void mapTagWithOrder() {
        for (int i = 0; i < tags.length; ++i) {
            map.put(tags[i], i + 1);
        }
    }
    
    public void loadModel() throws FileNotFoundException {
        loadPTransition();
        loadPEmit();
    }

    private void loadPTransition() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(Constant.PTRANSITION_PATH));
        int k = Constant.NUMBER_OF_TAGS;
        for (int i = 0; i <= k; ++i) {
            for (int j = 0; j <= k; ++j) {
                pTransition[i][j] = scanner.nextDouble();
            }
        }
    }

    private void loadPEmit() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(Constant.PEMIT_PATH));
        int k = Constant.NUMBER_OF_TAGS;
        long n = scanner.nextLong();
        scanner.nextLine();
        for (long i = 0; i < n; ++i) {
            String s = scanner.nextLine();
            String[] tokens = s.trim().split("[\\s]+");
            double[] p = new double[k + 1];
            for (int j = 2; j <= k + 1; ++j) {
                try {
                    p[j - 1] = Double.parseDouble(tokens[j]);
                } catch (NumberFormatException ex) {
                    System.out.println(s);
                    System.out.println(ex.getMessage());
                }
            }
            pEmit.put(tokens[0], p);
        }
    }

    public String tag(String sequence) {
        String[] w = sequence.split("[\\s]+");
        int m = w.length;
        int k = Constant.NUMBER_OF_TAGS;
        int[] t = new int[m];
        double maxP = 0;
        int argmax = 0;
        double[] p = new double[k + 1];;
        if (pEmit.containsKey(w[0].toLowerCase())) {
            p = pEmit.get(w[0].toLowerCase());
        } else {
            double x = (double) 1 / k;
            for (int j = 1; j <= k; ++j) {
                p[j] = x;
            }
        }
        for (int j = 1; j <= k; ++j) {
            double temp = p[j] * pTransition[j][0];
//            System.out.println(temp);
            if (temp > maxP) {
                argmax = j;
                maxP = temp;
            }
        }
        t[0] = argmax;

        for (int i = 1; i < m; ++i) {
            if (pEmit.containsKey(w[i].toLowerCase())) {
                p = pEmit.get(w[i].toLowerCase());
            } else {
                p = new double[k + 1];
                double x = (double) 1 / k;
                for (int j = 1; j <= k; ++j) {
                    p[j] = x;
                }
            }
            maxP = 0;
            argmax = 0;
            for (int j = 1; j <= k; ++j) {
                double temp = p[j] + pTransition[t[i - 1]][j];
                if (temp > maxP) {
                    argmax = j;
                    maxP = temp;
                }
            }
            t[i] = argmax;
        }

        String result = "";
        for (int i = 0; i < m; ++i) {
            result += w[i] + "/" + tags[t[i]] + " ";
        }
        return result;
    }

    public String viterbi(String sequence) {
        String[] w = sequence.split("[\\s]+");
        int n = w.length;
        int k = Constant.NUMBER_OF_TAGS;
        double[][] P = new double[n + 1][k + 1];
        String[][] L = new String[n + 1][k + 1];

        // Init
        double[] pe;
        if (pEmit.containsKey(w[0].toLowerCase())) {
            pe = pEmit.get(w[0].toLowerCase());
        } else {
            double x = (double) 1 / k;
            pe = new double[k + 1];
            for (int j = 1; j <= k; ++j) {
                pe[j] = x;
            }
        }
        
        for (int s = 1; s <= k; ++s) {
//            System.out.print(pe[s] + "|" + pTransition[s][0] + " ");
            P[1][s] = Math.log(pe[s]) + Math.log(pTransition[s][0]);
            L[1][s] = s + "|";
        }
//        System.out.println("a");

        // Loop
        for (int r = 2; r <= n; ++r) {
            if (pEmit.containsKey(w[r - 1].toLowerCase())) {
                pe = pEmit.get(w[r - 1].toLowerCase());
            } else {
                double x = 1.0 / k;
                pe = new double[k + 1];
                for (int j = 1; j <= k; ++j) {
                    pe[j] = x;
                }
            }
            int argmax = Util.indexMaxOfRow(P, r - 1);
            for (int s = 1; s <= k; ++s) {
//                System.out.print(pe[s] + "|" + pTransition[argmax][s] + " ");
                P[r][s] = P[r - 1][argmax] + Math.log(pe[s]) + Math.log(pTransition[argmax][s]);
                L[r][s] = L[r - 1][argmax] + s + "|";
            }
//            System.out.println("");
            
        }

        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= k; ++j) {
//                System.out.print(P[i][j] + "   ");
                
            }
//            System.out.println("");
        }
        // Result
        String tagSequence = L[n][Util.indexMaxOfRow(P, n)];
        String[] tagInedexes = tagSequence.split("[|]");
        String result = "";
        for (int i = 0; i < n; ++i) {
            result += w[i] + "/" + tags[Integer.parseInt(tagInedexes[i])] + " ";
        }
        return result;
    }
}
