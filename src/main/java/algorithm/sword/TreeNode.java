package algorithm.sword;

public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int x) {
        val = x;
    }


    static void preOrder(TreeNode root) {
        if(root == null) return;

        if(root.left != null) preOrder(root.left);
        System.out.print(" " + root.val);
        if(root.right != null) preOrder(root.right);
    }


}
