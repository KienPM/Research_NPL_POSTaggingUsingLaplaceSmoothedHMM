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
            System.out.println(tagger.viterbi("The dog saw the cat"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        String s = "fm 0.0 1.4438035560881586E-6 1.424264047160231E-6 1.3621213132484004E-6 1.4782250065411456E-6 1.4791630303908836E-6 1.3396044683846647E-6 2.778460816061726E-6 1.4745760962367344E-6 1.4765617963260188E-6 1.4794562702315646E-6 1.4642274589503832E-6 1.298377547416748E-6 1.3919841202451563E-6 5.390232628964685E-6 1.475507316303028E-6 1.478913649189851E-6 1.4658629828552665E-6 1.4532877000995503E-6 1.4668111964632248E-6 1.4332951599055746E-6 1.4768300139117388E-6 1.4788961519122128E-6 1.4769543429103976E-6 1.4794759696115637E-6 1.4456047832171067E-6 1.4794168730453204E-6 1.4394433384721461E-6 1.4341153086072733E-6 1.4565666393803184E-6 1.4492816635434647E-6 1.4603486144212345E-6 1.446746700694149E-6 1.4727258535550865E-6 1.475940690799281E-6 1.4792658699340691E-6 1.476311111898477E-6 1.468709151526723E-6 1.4793446503199082E-6 1.468581893266405E-6 1.4685754230965794E-6 1.4773274555398302E-6 1.4773056308981428E-6 1.408159155780423E-6 1.4763285480603995E-6 1.4717093315201874E-6";
//        String a[] = s.split("[\\s]+");
//        for (int i = 1; i < a.length; ++i) {
//            System.out.println(a[i] + " " + Double.parseDouble(a[i]));
//        }
    }
}
