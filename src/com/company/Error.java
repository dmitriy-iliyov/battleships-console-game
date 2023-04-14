package com.company;

import java.util.ArrayList;

public class Error {
    public boolean debug = true;
    public static void errMsg(String txt)
    {
        System.out.println("ERROR: "+txt + "!!!");
    }
    public static void printMatrix(int [][] matrix, String str)
    {
        System.out.println();
        System.out.println(str);
        for(int i = 0; i < matrix.length; i++)
        {
            for(int j = 0; j < matrix.length; j++)
            {
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void printMatrix(int [][] matrix)
    {
        System.out.println();
        for(int i = 0; i < matrix.length; i++)
        {
            for(int j = 0; j < matrix.length; j++)
            {
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void printArray(int [] matrix)
    {
        System.out.println();
        for(int i = 0; i < matrix.length; i++)
        {
            System.out.print(matrix[i] + "  ");
            System.out.println();
        }
//new int [] {side, x, y}
    }
    public static void printList(ArrayList<int[]> list)
    {
        System.out.println();
        for( int[] array: list)
        {
            for (int x: array) {
                System.out.println(x);
            }
        }
        System.out.println();
    }
    public static void printStr(String str)
    {
        System.out.println(str);
    }

}
