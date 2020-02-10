public class BinTreeNode<T> {


    private AnyType element;

    private BinTreeNode left;

    private BinTreeNode right;

    public BinTreeNode(AnyType anyType,BinTreeNode left, BinTreeNode right){
        element = anyType;
        left = left;
        right = right;
    }

    private BinTreeNode insert(AnyType anyType, BinTreeNode b) {

        if (b == null) {
            return new BinTreeNode(anyType,null,null);
        }
        int re = anyType.compareTo(b.element);
        if (re < 0) {
            b.left = insert(anyType,b.left);
        }
        if (re > 0) {
            b.right = insert(anyType,b.right);
        }
        else {
            //do somthing;
            return null;
        }
        return b;
    }

    public BinTreeNode remove(AnyType anyType, BinTreeNode b){
        if (b == null) {
            return null;
        }
        int re = anyType.compareTo(b.element);
        if (re < 0) {
            b.left = remove(anyType,b.left);
        }
        if (re > 0) {
            b.right = remove(anyType,b.right);
        }
        else if(b.left !=null && b.right != null){
            //删除策略，找出右子树的最小值，覆盖到当前节点。并递归的删除那个节点
            b.element = findMin(b.right);
            b.right = remove(b.element,b.right);
        }
        else {
            b = b.left != null ? b.left : b.right;
        }
        return b;
    }

    //子树节点的最小值
    private AnyType findMin(BinTreeNode t){
        if(t.left == null){
            return t.element;
        }
        return findMin(t.left);
    }
    class AnyType implements Comparable {

        @Override
        public int compareTo(Object o) {
            return 0;
        }
    }
}
