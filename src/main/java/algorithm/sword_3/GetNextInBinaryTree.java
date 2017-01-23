package algorithm.sword_3;

/**
 * Created by xinszhou on 21/01/2017.
 */
public class GetNextInBinaryTree {
    // 可以画了一个图帮忙理解题意
    // 中序遍历为左中右，
    public TreeLinkNode GetNext(TreeLinkNode pNode) {
        if (pNode == null) return null;

        if (pNode.right != null) { // next 节点在右子树的最左节点上
            TreeLinkNode rightChild = pNode.right;
            while (rightChild.left != null) {
                rightChild = rightChild.left;
            }
            return rightChild;
        } else { // next 节点在父亲节点, 这也分为几种情况，要根据父节点是否是
            TreeLinkNode parent = pNode.next;
            TreeLinkNode current = pNode;
            while (parent != null) {
                if (current == parent.left) {
                    return parent;
                } else {
                    current = parent;
                    parent = current.next;
                }

            }
            return null;
        }
    }

    public class TreeLinkNode {
        int val;
        TreeLinkNode left = null;
        TreeLinkNode right = null;
        TreeLinkNode next = null;

        TreeLinkNode(int val) {
            this.val = val;
        }
    }


}
