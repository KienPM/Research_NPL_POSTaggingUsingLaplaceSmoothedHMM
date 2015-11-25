/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Ken
 */
public class Test {

    public static void main(String[] args) {
        String s = "   d  ddd            dd   ";
        String[] a = s.trim().split("[\\s]+");
        for (int i = 0; i < a.length; ++i) {
            System.out.println(a[i]);
        }
    }
}
