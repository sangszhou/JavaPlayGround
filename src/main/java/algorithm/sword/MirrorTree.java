package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class MirrorTree {
    public void Mirror(TreeNode root) {
        if(root == null) return;

        Mirror(root.left);
        Mirror(root.right);

        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
    }

    public void doMirror(TreeNode originRoot, TreeNode newRoot) {

    }

}
