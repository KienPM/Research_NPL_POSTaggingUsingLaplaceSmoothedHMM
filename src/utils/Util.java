/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import model.Constant;

/**
 *
 * @author Ken
 */
public class Util {
    public static String toTaggedForm(String input) {
        return input.replaceAll("[=\\[\\]]", " ");
    }
    
    public static String[] loadPennTreebank() throws FileNotFoundException {
        String[] result = new String[Constant.NUMBER_OF_TAGS];
        Scanner scanner = new Scanner(new FileInputStream(Constant.PENN_TREEBANK_PATH));
        for (int i = 0; i < Constant.NUMBER_OF_TAGS; ++i) {
            result[i] = scanner.nextLine().trim();
        }
        return result;
    }
}
