/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldmodel;

import java.text.NumberFormat;

/**
 *
 * @author Ken
 */
public class ViterbiMatrixTools {
    static double sumForCol(int i, double[][] matrix) {
	    double sum = 0;
	    for (int j = 0; j < matrix.length; j++) {
	        sum += matrix[j][i];
	    }
	    return sum;
	}
	
	static double sumForRow(int i, double[][] matrix) {
	    double sum = 0;
	    for (int j = 0; j < matrix[i].length; j++) {
	        sum += matrix[i][j];
	    }
	    return sum;
	}
	
	static double maximimumForCol(int i, double[][] matrix) {
	    double maxValue = 0.0;
	    for (int j = 0; j < matrix.length; j++) {
	        maxValue = Math.max(maxValue, matrix[j][i]);
	    }
	    return maxValue;
	}
	
	static int indexOfMaximimumForCol(int i, double[][] matrix) {
	    int maxIndex = -1;
	    double maxValue = -1.0;
	    for (int j = 0; j < matrix.length; j++) {
	        if (matrix[j][i] > maxValue) {
	            maxIndex = j;
	            maxValue = matrix[j][i];
	        }
	    }
	    return maxIndex;
	}
	
	static int indexOfMaximimumForCol(int i, int[][] matrix) {
	    int maxIndex = -1;
	    int maxValue = -1;
	    for (int j = 0; j < matrix.length; j++) {
	        if (matrix[j][i] > maxValue) {
	            maxIndex = j;
	            maxValue = matrix[j][i];
	        }
	    }
	    return maxIndex;
	}
	
	static void printMatrix(double[][] matrix) {
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix[i].length; j++) {
	            String myString = NumberFormat.getInstance().format(
	                    matrix[i][j]);
	            if (myString.length() < 5) {
	                for (int k = myString.length(); k < 5; k++) {
	                    myString += " ";
	                }
	            }
	            System.out.print(myString + "   ");
	        }
	        System.out.println();
	    }
	}
	
	static void printMatrix(int[][] matrix) {
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix[i].length; j++) {
	            if (matrix[i][j] >= 0)
	                System.out.print(" ");
	            System.out.print(matrix[i][j] + "   ");
	        }
	        System.out.println();
	    }
	}
}
