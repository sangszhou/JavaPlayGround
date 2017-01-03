package algorithm.sword_2;

import algorithm.sword.TreeNode;
import apple.laf.JRSUIUtils;

/**
 * Created by xinszhou on 17/12/2016.
 */
public class IsSymmetrical {

    boolean isSymmetrical(TreeNode pRoot) {
        if (pRoot == null) {
            return true;
        }
        return compare(pRoot.left, pRoot.right);
    }

    boolean compare(TreeNode r1, TreeNode r2) {
        if(r1 == null && r2 == null) return true;
        if(r1 == null && r2 != null) return false;
        if(r1 != null && r2 == null) return false;

        if(r1.val != r2.val) return false;
        if(r1 == null) return true;

        return compare(r1.left, r2.right) && compare(r1.right, r2.left);
    }

}
