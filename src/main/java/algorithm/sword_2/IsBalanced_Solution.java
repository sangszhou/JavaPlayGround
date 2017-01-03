package algorithm.sword_2;

import algorithm.sword.TreeNode;

/**
 * Created by xinszhou on 16/12/2016.
 */
public class IsBalanced_Solution {

    public boolean IsBalanced_Solution(TreeNode root) {
        boolean[] rst = new boolean[0];
        rst[0] = true;

        doDepth(root, rst);

        return rst[0];

    }

    private int doDepth(TreeNode root, boolean[] rst) {
        if (root == null) {
            return 0;
        }

        if(!rst[0]) return 0;

        int l = doDepth(root.left, rst);
        int r = doDepth(root.right, rst);

        if (Math.abs(l - r) > 1) {
            rst[0] = false;
        }

        return 1 + Math.max(l, r);
    }

}
