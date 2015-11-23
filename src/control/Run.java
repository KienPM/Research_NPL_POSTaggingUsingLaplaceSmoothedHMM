/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Tagger;
import model.Trainer;

/**
 *
 * @author Ken
 */
public class Run {

    public static void main(String[] args) {

//        File f = new File("G:\\Training data\\POS tagging\\wsj");
//        File[] files = f.listFiles();
//        Trainer trainer;
//        try {
//            trainer = new Trainer();
//            trainer.train(files);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        try {
            Tagger tagger = new Tagger();
            System.out.println(tagger.viterbi("Time flies like an arrow"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
