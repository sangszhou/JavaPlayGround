package algorithm.sword_2;

import algorithm.sword.TreeNode;

/**
 * Created by xinszhou on 16/12/2016.
 */
public class TreeDepth {

    public int TreeDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftDepth = TreeDepth(root.left);
        int rightDepth = TreeDepth(root.right);

        return 1 + Math.max(leftDepth, rightDepth);

    }

}
