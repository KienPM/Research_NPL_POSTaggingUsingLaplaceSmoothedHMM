/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldmodel;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Ken
 */
public class ViterbiHMM extends AbstractHMM {
    private double[][] delta;
    private int[][] psi;
    private String[] hiddenAlphabet;
    private String[] observableAlphabet;
    private String[] observation;
    private boolean absoluteValues;
    public ViterbiHMM(String a, String obsA, String obs,
            Map<String, String> lexicon, String corpus, boolean absoluteValues) {
        super(a, obsA, ". " + obs, lexicon, corpus);
        this.absoluteValues = absoluteValues;
    }
    public String mostProbableSequence() {
        
	init();
	induct();
        System.out.println("Delta:");
        ViterbiMatrixTools.printMatrix(delta);
        System.out.println();
        System.out.println("Psi:");
        ViterbiMatrixTools.printMatrix(psi);
        System.out.println();
        return getResult();
    }
    
    private void init() {
        hiddenAlphabet = a.split("\\s");
        observableAlphabet = obsA.split("\\s");
        observation = obs.split("\\s");
        delta = new double[hiddenAlphabet.length][observation.length];
        delta[delta.length - 1][0] = 1.0;
        psi = new int[hiddenAlphabet.length][observation.length - 1];
        for (int i = 0; i < psi.length; i++) {
            Arrays.fill(psi[i], -1);
        }
    }
    
    private void induct() {
        // (1)
        for (int i = 1; i < observation.length; i++) {
            // (2)
            for (int j = 0; j < hiddenAlphabet.length; j++) {
                double prevTagMax = ViterbiMatrixTools.maximimumForCol(
                        i - 1, delta);
                // (3)
                int lexIndex = getIndex(observation[i], observableAlphabet);
                // (4)
                double prob = probForWordToTag(lexIndex + 1, j, B, A);
                double res = prevTagMax * prob;
                delta[j][i] = res;
                if (res > 0.0)
                    psi[j][i - 1] = ViterbiMatrixTools.indexOfMaximimumForCol(
                            i - 1, delta);
            }

        }
    }
    
    private double probForWordToTag(int i, int j, double[][] b, double[][] a) {
        // delta has the leading period, therefore - 1 for previous:
        int prevIndex = ViterbiMatrixTools.indexOfMaximimumForCol(i - 1, delta);
        // b doesn't have the leading p, therefore -1 for recent:
        if (absoluteValues)
            return (b[i - 1][j] / ViterbiMatrixTools.sumForCol(j, B))
                    * (A[prevIndex][j] / ViterbiMatrixTools.sumForRow(
                            prevIndex, A));
        else {
            return b[i - 1][j] * A[prevIndex][j];
        }
    }
    
    private String getResult() {
        String[] resultArray = new String[psi[0].length];
        int lastIndexInPsi = ViterbiMatrixTools.indexOfMaximimumForCol(
                delta[0].length - 1, delta);
        if (lastIndexInPsi == -1) {
            System.out.println("no tag-sequence found for input, exit.");
            System.exit(0);
        }
        int lastValueInPsi = psi[lastIndexInPsi][psi[0].length - 1];
        String lastTag = hiddenAlphabet[lastIndexInPsi];
        resultArray[resultArray.length - 1] = lastTag;
        // retrieve other tags:
        for (int i = psi[0].length - 2; i >= 0; i--) {
            resultArray[i] = hiddenAlphabet[lastValueInPsi];
            lastValueInPsi = psi[lastValueInPsi][i];
        }
        StringBuffer resultString = new StringBuffer();
        for (int i = 0; i < resultArray.length; i++) {
            resultString.append(resultArray[i]);
            if (i < resultArray.length - 1)
                resultString.append(" ");
        }
        return resultString.toString();
    }
    
    int getIndex(String string, String[] lexicon) {
        for (int i = 0; i < lexicon.length; i++) {
            if (string.equals(lexicon[i]))
                return i;
        }
        System.out.println("Word '" + string + "' not found in lexicon, exit.");
        System.exit(0);
        return -1;
    }
}
