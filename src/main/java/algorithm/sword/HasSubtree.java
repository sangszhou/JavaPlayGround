package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 * 这道题易错， 需要注意 directSubtree 这一特殊情况
 */
public class HasSubtree {
    public boolean HasSubtree(TreeNode root1,TreeNode root2) {
        if (root2 == null) {
            return false;
        }
        if (root1 == null) {
            return false;
        }

        if (root1.val == root2.val) {
            boolean leftMatch = true, rightMatch = true;

            if(root2.left != null) {
                leftMatch = directSubtree(root1.left, root2.left);
            }

            if (root2.right != null) {
                rightMatch = directSubtree(root1.right, root2.right);
            }

            if(leftMatch && rightMatch) return true;
        }

        if (HasSubtree(root1.left, root2)) return true;
        if (HasSubtree(root1.right, root2)) return true;

        return false;
    }

    private boolean directSubtree(TreeNode root1, TreeNode root2) {
        if(root2 == null) return true;
        if(root1 == null) return false;

        return root1.val == root2.val
                && directSubtree(root1.left, root2.left)
                && directSubtree(root1.right, root2.right);

    }
}
