package algorithm.sword;

import apple.laf.JRSUIUtils;

/**
 * Created by xinszhou on 04/12/2016.
 */
// @todo
public class ConstructTreeFromPreInOrder {

    // 前序{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}
    // 好像 pre.length 就行不通
    public TreeNode reConstructBinaryTree(int [] pre,int [] in) {
        return doReConstructBTree(pre, 0, pre.length-1, in, 0, in.length-1);
    }

    private TreeNode doReConstructBTree(int[] pre, int preS, int preE, int[] in, int inS, int inE) {
        if(preS > preE) return null;

        if (preE == preS) {
            return new TreeNode(pre[preS]);
        }

        int mid = in[inS];
        int i = preS;
        for(; i <= preE; i ++) {
            if (pre[i] == mid) {
                break;
            }
        }

        TreeNode midNode = new TreeNode(mid);
        midNode.left  = doReConstructBTree(pre, preS, i-1, in, inS + 1, inS + i - preS);
        midNode.right = doReConstructBTree(pre, i + 1, preE, in, inS + i - preS + 1, inE);

        return midNode;
    }

    public static void main(String args[]) {
        int[] pre = new int[6];
        int[] in  = new int[6];
        pre[0] = 4; pre[1] = 2; pre[2] = 5; pre[3] = 1; pre[4] = 6; pre[5] = 3;
        in[0] = 1; in[1] = 2; in[2] = 4; in[3] = 5; in[4] = 3; in[5] = 6;

        TreeNode root = new ConstructTreeFromPreInOrder().reConstructBinaryTree(pre, in);

        TreeNode.preOrder(root);
        System.out.println("");
        
    }

}
