/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldmodel;

import java.util.Map;

/**
 *
 * @author Ken
 */
public abstract class AbstractHMM {
    protected String a;
    protected int n; // number of states
    protected String obsA; 
    protected String obs; 
    protected double[][] A; // state transition probabilities matrix
    protected double[][] B; // observation probabilities matrix
    protected String mostProbableSequence;
    public AbstractHMM(String a, String obsA, String obs,
            Map<String, String> lexicon, String corpus) {
        MatrixGenerator gen = new MatrixGenerator(lexicon, corpus, a);
        this.a = a;
        this.n = obs.length();
        this.obsA = obsA;
        this.obs = obs;
        this.A = gen.createMatrixA(0.01);
        this.B = gen.createMatrixB();
    }

    public abstract String mostProbableSequence();
}
