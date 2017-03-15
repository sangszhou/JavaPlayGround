package algorithm.cracking;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xinszhou on 15/02/2017.
 * 这道题的大致思路是对的，但是对题意的理解不对，皇后不能在一条直线上
 */

//八皇后问题是一个以国际象棋为背景的问题：如何能够在8×8的国际象棋棋盘上放置八个皇后，使得任何一个皇后都无法直接吃掉其他的皇后？为了达到此目的，任两个皇后都不能处于同一条横行、纵行或斜线上。八皇后问题可以推广为更一般的n皇后摆放问题：这时棋盘的大小变为n×n，而皇后个数也变成n。当且仅当n = 1或n ≥ 4时问题有解[1]
public class NQueue {
    public int nQueens(int n) {
        // write code here
        List<Integer> filled = new LinkedList<>();

        return doQueue(0, n, filled);
    }

    public int doQueue(int current, int endline, List<Integer> filled) {
        if (current == endline) {
            return 1;
        }
        int result = 0;

        for (int i = 0; i < endline; i++) {
            if (filled.contains(i)) {
                continue;
            } else {
                filled.add(i);
                result += doQueue(current + 1, endline, filled);
                filled.remove(filled.size() - 1);
            }
        }
        return result;
    }

    public static void main(String args[]) {
        NQueue q = new NQueue();
        int result = q.nQueens(10);
        System.out.println(result);
    }
}
