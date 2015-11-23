/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Constant;
import model.Tagger;
import model.Trainer;
import view.UI;
import vn.hus.nlp.utils.UTF8FileUtility;

/**
 *
 * @author Ken
 */
public class Controler {

    private final UI view;
    private File[] trainingFolders, testingFolders;
    private Trainer trainer;
    private Tagger tagger;
    private boolean isLoadedModel;

    public Controler() throws FileNotFoundException {
        view = new UI();
        addListener();
        loadPennTreeBankDetail();
        view.setVisible(true);
        trainer = new Trainer();
        tagger = new Tagger();
        isLoadedModel = false;
    }

    private void loadPennTreeBankDetail() {
        DefaultTableModel dtm = (DefaultTableModel) view.getTblPennTreebank().getModel();
        try {
            String[] lines = UTF8FileUtility.getLines(Constant.PENN_TREEBANK_DETAIL_PATH);
            for (int i = 0; i < lines.length; ++i) {
                int index = lines[i].indexOf(" ");
                Object[] row = new Object[]{i + 1, lines[i].subSequence(0, index),
                    lines[i].substring(index + 1)};
                dtm.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addListener() {
        view.getBtnBrowseTrainingFolders().addActionListener((ActionEvent e) -> {
            onClickBrowseTrainingFolders();
        });
        view.getBtnTrain().addActionListener((ActionEvent e) -> {
            onClickTrain();
        });
        view.getBtnBrowseTestingFolders().addActionListener((ActionEvent e) -> {
            onClickBrowseTestingFolders();
        });
        view.getBtnLoadModel().addActionListener((ActionEvent e) -> {
            onClickLoadModel();
        });
        view.getBtnTest().addActionListener((ActionEvent e) -> {
            onClickTest();
        });
        view.getBtnTag().addActionListener((ActionEvent e) -> {
            onClickTag();
        });
    }

    public void onClickBrowseTrainingFolders() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("G:\\Training data\\POS tagging\\wsj"));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(view);
        trainingFolders = fileChooser.getSelectedFiles();
        if (trainingFolders != null) {
            String s = "";
            for (int i = 0; i < trainingFolders.length; ++i) {
                s += trainingFolders[i].getName() + ";";
            }
            view.getTxtTrainingFoldersPath().setText(s);
        }
    }

    public void onClickTrain() {
        view.getTxaStatus().setText("Training...\nWaiting...\n");
        trainer.train(trainingFolders);
        try {
            tagger.loadModel();
            isLoadedModel = true;
            view.getTxaStatus().append("Done!");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(view, "Load model failed!");
        }
    }

    public void onClickLoadModel() {
        try {
            tagger.loadModel();
            isLoadedModel = true;
            JOptionPane.showMessageDialog(view, "Model has been loaded!");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(view, "Load model failed!");
        }
    }

    public void onClickBrowseTestingFolders() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("G:\\Training data\\POS tagging\\wsj"));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(view);
        testingFolders = fileChooser.getSelectedFiles();
        if (testingFolders != null) {
            String s = "";
            for (int i = 0; i < testingFolders.length; ++i) {
                s += testingFolders[i].getName() + ";";
            }
            view.getTxtTrainingFoldersPath().setText(s);
        }
    }

    public void onClickTest() {

    }

    public void onClickTag() {
        if (!isLoadedModel) {
            try {
                tagger.loadModel();
                isLoadedModel = true;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(view, "Load model failed!");
                return;
            }
        }
        if (view.getRadPlainText().isSelected()) {
            String input = view.getTxaPlaintextInput().getText();
            String output = tagger.viterbi(input);
            view.getTxaOutput().setText(output);
        } else {
            String input = view.getTxaTaggedInput().getText();
            String[] temp = input.split("[\\s]+");
            input = "";
            for (int i = 0; i < temp.length; ++i) {
                input += temp[i].substring(0, temp[i].indexOf("/")) + " ";
            }
            String output = tagger.viterbi(input);
            view.getTxaOutput().setText(output);
        }
    }
}
