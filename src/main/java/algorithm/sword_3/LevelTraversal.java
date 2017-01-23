package algorithm.sword_3;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by xinszhou on 21/01/2017.
 */
public class LevelTraversal {
    ArrayList<ArrayList<Integer>> Print(TreeNode pRoot) {

        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

        if (pRoot == null) return result;

        int levelCount = 1, nextLevelCount = 0;

        Queue<TreeNode> queue = new ArrayDeque<TreeNode>();
        queue.add(pRoot);

        ArrayList<Integer> currentLevelList = new ArrayList<Integer>();

        while (!queue.isEmpty()) {
            levelCount--;
            TreeNode node = queue.poll();
            currentLevelList.add(node.val);

            if (node.left != null) {
                queue.add(node.left);
                nextLevelCount++;
            }
            if (node.right != null) {
                queue.add(node.right);
                nextLevelCount++;
            }

            if (levelCount == 0) {
                levelCount = nextLevelCount;
                nextLevelCount = 0;
                result.add(currentLevelList);
                currentLevelList = new ArrayList<Integer>();
            }
        }

        return result;
    }

    public class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }

    }


}
