package data;

//https://blog.csdn.net/qq_34117624/article/details/82694878
public class BinomiaQueue<AnyType extends Comparable<? super AnyType>> {
    private static final int DEFAULT_TREES = 1;
    private int currentSize;
    private Node<AnyType>[] theTrees;

    public BinomiaQueue() {
        theTrees = new Node[DEFAULT_TREES];
    }

    public BinomiaQueue(AnyType a) {
        this();
        theTrees[0] = new Node(a);
        currentSize = 1;
    }

    public void merge(BinomiaQueue<AnyType> rhs) {
        if (rhs == null) {
            return;
        }
        currentSize += rhs.currentSize;
        if (currentSize > capacity()) {
            int maxLength = Math.max(theTrees.length, rhs.theTrees.length);
            expandTheTrees(maxLength + 1);
        }
        Node<AnyType> carry = null;
        for (int i = 0, j = 1; j <= currentSize; i++, j *= 2) {
            Node<AnyType> t1 = theTrees[i];
            Node<AnyType> t2 = i < rhs.theTrees.length ? rhs.theTrees[i] : null;

            int whichCase = t1 == null ? 0 : 1;
            whichCase += t2 == null ? 0 : 2;
            whichCase += carry == null ? 0 : 4;

            switch (whichCase) {
                case 0:
                case 1:
                    break;
                case 2:
                    theTrees[i] = t2;
                    rhs.theTrees[i] = null;
                    break;
                case 4:
                    theTrees[i] = carry;
                    carry = null;
                    break;
                case 3:
                    carry = combineTrees(t1, t2);
                    theTrees[i] = rhs.theTrees[i] = null;
                    break;
                case 5:
                    carry = combineTrees(t1, carry);
                    theTrees[i] = null;
                    break;
                case 6:
                    carry = combineTrees(t2, carry);
                    rhs.theTrees[i] = null;
                    break;
                case 7:
                    theTrees[i] = carry;
                    carry = combineTrees(t1, t2);
                    rhs.theTrees[i] = null;
                    break;
            }
        }
        for (int i = 0; i < rhs.theTrees.length - 1; i++) {
            rhs.theTrees[i] = null;
        }
        rhs.currentSize = 0;
    }

    public void insert(AnyType a) {
        merge(new BinomiaQueue<>(a));
    }

    public AnyType findMin() {
        AnyType min = theTrees[0].element;
        for (int i = 1; i < theTrees.length; i++) {
            Node<AnyType> node = theTrees[i];
            if (node != null && min.compareTo(node.element) > 0) {
                min = theTrees[i].element;
            }
        }
        return min;
    }

    private int findMinIndex() {
        AnyType min = findMin();
        for (int i = 0; i < theTrees.length; i++) {
            Node<AnyType> node = theTrees[i];
            if (node != null && node.element.compareTo(min) == 0) {
                return i;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public AnyType deleteMin() {
        int minIndex = findMinIndex();
        AnyType minItem = theTrees[minIndex].element;
        Node<AnyType> deleteTree = theTrees[minIndex].leftChild;

        BinomiaQueue<AnyType> deleteQueue = new BinomiaQueue<>();
        deleteQueue.expandTheTrees(minIndex + 1);

        deleteQueue.currentSize = (1 << minIndex) - 1;
        for (int j = minIndex - 1; j >= 0; j--) {
            deleteQueue.theTrees[j] = deleteTree;
            deleteTree = deleteTree.nextSibling;
            deleteQueue.theTrees[j].nextSibling = null;
        }
        theTrees[minIndex] = null;
        currentSize -= deleteQueue.currentSize + 1;
        merge(deleteQueue);
        return minItem;
    }

    private Node<AnyType> combineTrees(Node<AnyType> t1, Node<AnyType> t2) {
        if (t1.element.compareTo(t2.element) > 0) {
            return combineTrees(t2, t1);
        }
        t2.nextSibling = t1.leftChild;
        t1.leftChild = t2;
        return t1;
    }

    private void expandTheTrees(int newNumTrees) {
        Node<AnyType>[] newTrees = new Node[newNumTrees];
        for (int i = 0; i < Math.min(theTrees.length, newNumTrees); i++) {
            newTrees[i] = theTrees[i];
        }
        theTrees = newTrees;
    }

    private int capacity() {
        return (1 << theTrees.length - 1);
    }

    private static class Node<AnyType> {
        AnyType element;
        Node<AnyType> leftChild;
        Node<AnyType> nextSibling;

        public Node(AnyType a, Node<AnyType> l, Node<AnyType> n) {
            this.element = a;
            this.leftChild = l;
            this.nextSibling = n;
        }

        public Node(AnyType a) {
            this(a, null, null);
        }
    }

}