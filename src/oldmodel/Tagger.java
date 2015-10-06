/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldmodel;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ken
 */
public class Tagger {

    String tagset;
    String corpus;
    Map<String, String> lexicon;
    String lexiconString;

    private void initData() {
        lexicon = new HashMap<String, String>();
        lexicon.put("fly", "vn");
        lexicon.put("layers", "vn");
        lexicon.put("sneaks", "v");
        lexicon.put("food", "n");
        lexicon.put("that", "det,rp");
        lexicon.put(".", "period");
        StringBuffer lexBuf = new StringBuffer();
        for (String word : lexicon.keySet()) {
            lexBuf.append(word + " ");
        }
        lexiconString = lexBuf.toString();
        tagset = "det rp vn v n period";
        corpus = "that fly layers. that fly sneaks. that, that sneaks food"
                + "layers. that fly layers that food. layers.";
    }

    public String tag(String input) {
        initData();
        ViterbiHMM hmm = new ViterbiHMM(tagset, lexiconString, input, lexicon,
                corpus, true);
        String[] in = input.split(" ");
        String[] re = hmm.mostProbableSequence().split(" ");
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < in.length; i++) {
            buf.append(in[i] + ":" + re[i] + " ");
        }
        return buf.toString();
    }

}
