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

    public static long sumRow(long[][] a, int row) {
        long sum = 0;
        for (int i = 0; i < a.length; ++i) {
            sum += a[row][i];
        }
        return sum;
    }

    public static long sumColumn(long[][] a, int column) {
        long sum = 0;
        for (int i = 0; i < a.length; ++i) {
            sum += a[i][column];
        }
        return sum;
    }

    public static double sumRow(double[][] a, int row) {
        double sum = 0;
        for (int i = 0; i < a.length; ++i) {
            sum += a[row][i];
        }
        return sum;
    }

    public static double sumColumn(double[][] a, int column) {
        double sum = 0;
        for (int i = 0; i < a.length; ++i) {
            sum += a[i][column];
        }
        return sum;
    }
    
    public static int indexMaxOfRow(double[][] a, int row) {
        int index = 1;
        for (int j = 2; j < a[0].length; ++j) {
            if (a[row][j] < a[index][j]) {
                index = j;
            }
        }
        return index;
    }
}
