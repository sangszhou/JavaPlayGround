package algorithm.sword_2;

import algorithm.sword.TreeNode;

/**
 * Created by xinszhou on 17/12/2016.
 */
public class KthNode {

    public static void main(String args[]) {
        TreeNode root = new TreeNode(8);
        root.left = new TreeNode(6);
        root.right = new TreeNode(10);

        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(7);

        root.right.left = new TreeNode(9);
        root.right.right = new TreeNode(11);

        TreeNode n = new KthNode().KthNode(root, 5);

        System.out.println(n.val);
    }

    TreeNode KthNode(TreeNode pRoot, int k) {
        if(k == 0) return null;
        if(pRoot == null) return null;

        return doKthNode(pRoot, k, new int[1]);
    }

    TreeNode doKthNode(TreeNode pRoot, int k, int[] num) {
        if(pRoot == null) {
            num[0] = 0;
            return null;
        }

        int[] cpNum = new int[]{0};
        int count = 0;

        TreeNode candi = doKthNode(pRoot.left, k, cpNum);

        if (candi != null) {
            return candi;
        }

        if(cpNum[0] + 1 == k) {
            return pRoot;
        }

        count += cpNum[0] + 1;

        candi = doKthNode(pRoot.right, k - cpNum[0] - 1, cpNum);

        count += cpNum[0];
        num[0] = count;

        return candi;

    }

}
