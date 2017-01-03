package algorithm.sword;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class PrintMatrixSeq {
    /**
     * 这道题稍微复杂些
     * 有两个思路，第一个思路是使用方向指示器
     * 第二个思路是把转圈打印的过程看成两步的组合，里层是打印一圈，外层是控制打印的起始位置
     * 第二个方案更好 debug 吧
     */

    public ArrayList<Integer> printMatrix(int [][] matrix) {
        if(matrix == null || matrix[0] == null ||
                matrix.length == 0 || matrix[0].length == 0) return new ArrayList<>();

        int x = matrix.length, y = matrix[0].length;

        ArrayList<Integer> array = new ArrayList<>();

        for (int i = 0; i < (y+1)/2; i ++) {
            int leftX = 0;
            int leftY = 0;
            int rightX = 0;
            int rightY = 0;

            array.addAll(printCycle(matrix, leftX, leftY, rightX, rightY));
        }

        return array;
    }

    private ArrayList<Integer> printCycle(int[][] matrix, int leftX, int leftY, int rightX, int rightY) {

        ArrayList<Integer> collected = new ArrayList<>();

        // top left to right
        for(int i = leftY; i <= rightY; i ++) {
            collected.add(matrix[leftX][i]);
        }

        // right top to bottom
        for(int i = leftX; i <= rightX; i ++) {
            collected.add(matrix[i][rightY]);
        }

        // bottom right to left
        for(int i = rightY; i >= leftY; i --) {
            collected.add(matrix[rightX][i]);
        }

        // left bottom to top
        for(int i = rightX; i >= leftX; i --) {
            collected.add(matrix[i][leftY]);
        }

        return collected;
    }

}
