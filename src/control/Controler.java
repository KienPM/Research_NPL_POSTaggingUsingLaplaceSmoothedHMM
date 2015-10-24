/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import javax.swing.table.DefaultTableModel;
import model.Constant;
import view.UI;
import vn.hus.nlp.utils.UTF8FileUtility;

/**
 *
 * @author Ken
 */
public class Controler {

    private final UI view;

    public Controler() {
        view = new UI();
        loadPennTreeBankDetail();
        view.setVisible(true);
    }

    private void loadPennTreeBankDetail() {
        DefaultTableModel dtm = (DefaultTableModel) view.getTblPennTreebank().getModel();
        try {
            String[] lines = UTF8FileUtility.getLines(Constant.PENN_TREEBANK_DETAIL_PATH);
            for (int i = 0; i < lines.length; ++i) {
                int index = lines[i].indexOf(" ");
                Object[] row = new Object[] {i + 1, lines[i].subSequence(0, index),
                                            lines[i].substring(index + 1)};
                dtm.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
