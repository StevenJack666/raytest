package data;


//https://www.cnblogs.com/hapjin/p/5468817.html
public class BinomialQueue<AnyType extends Comparable<? super AnyType>> {
    private static final int DEFAULT_TREES = 1;

    private int currentSize;                // # items in priority queue
    private BinNode<AnyType>[] theTrees;  // An array of tree roots

    /**
     * Construct the binomial queue.
     */
    public BinomialQueue( BinomialQueue<AnyType> anyType) {
        theTrees = new BinNode[DEFAULT_TREES];
        makeEmpty();
    }

    /**
     * Construct the binomial queue.
     */
    public BinomialQueue() {
        theTrees = new BinNode[DEFAULT_TREES];
        makeEmpty();
    }


    private static class BinNode<AnyType> {
        AnyType element;     // The data in the node
        BinNode<AnyType> leftChild;   // Left child
        BinNode<AnyType> nextSibling; // Right child

        // Constructors
        BinNode(AnyType theElement) {
            this(theElement, null, null);
        }

        BinNode(AnyType theElement, BinNode<AnyType> leftChild, BinNode<AnyType> nextSibling) {
            element = theElement;     // The data in the node
            leftChild = leftChild;   // Left child
            nextSibling = nextSibling; // Right child
        }
        //other operations.....
    }
    //other operations.....

    /**
     * Return the result of merging equal-sized t1 and t2.
     */
    private BinNode<AnyType> combineTrees(BinNode<AnyType> t1, BinNode<AnyType> t2) {
        if (t1.element.compareTo(t2.element) > 0)
            return combineTrees(t2, t1);//��һ������t1���Ǵ�������Ȩֵ��С���ǿŶ�����
        t2.nextSibling = t1.leftChild;//��Ȩֵ��Ķ�������������ΪȨֵС�Ķ����������ֵ�
        t1.leftChild = t2;//��ȨֵС�Ķ����� ��Ϊ Ȩֵ��� ������ �� ����
        return t1;
    }

    /**
     * Merge rhs into the priority queue. �ϲ�this �� rhs �������������
     * rhs becomes empty. rhs must be different from this.
     *
     * @param rhs the other binomial queue.
     */
    public void merge(BinomialQueue<AnyType> rhs) {
        if (this == rhs)    // Avoid aliasing problems.��֧��������ͬ�Ķ�����кϲ�
            return;

        currentSize += rhs.currentSize;//�ºϲ���Ķ�������еĽ�����

        if (currentSize > capacity()) {
            int newNumTrees = Math.max(theTrees.length, rhs.theTrees.length) + 1;
            expandTheTrees(newNumTrees);
        }

        BinNode<AnyType> carry = null;
        for (int i = 0, j = 1; j <= currentSize; i++, j *= 2) {
            BinNode<AnyType> t1 = theTrees[i];
            BinNode<AnyType> t2 = i < rhs.theTrees.length ? rhs.theTrees[i] : null;
            //�ϲ���8�����
            int whichCase = t1 == null ? 0 : 1;
            whichCase += t2 == null ? 0 : 2;
            whichCase += carry == null ? 0 : 4;

            switch (whichCase) {
                case 0: /* No trees */
                case 1: /* Only this */
                    break;
                case 2: /* Only rhs */
                    theTrees[i] = t2;
                    rhs.theTrees[i] = null;
                    break;
                case 4: /* Only carry */
                    theTrees[i] = carry;
                    carry = null;
                    break;
                case 3: /* this and rhs */
                    carry = combineTrees(t1, t2);
                    theTrees[i] = rhs.theTrees[i] = null;
                    break;
                case 5: /* this and carry */
                    carry = combineTrees(t1, carry);
                    theTrees[i] = null;
                    break;
                case 6: /* rhs and carry */
                    carry = combineTrees(t2, carry);
                    rhs.theTrees[i] = null;
                    break;
                case 7: /* All three */
                    theTrees[i] = carry;
                    carry = combineTrees(t1, t2);
                    rhs.theTrees[i] = null;
                    break;
            }
        }

        for (int k = 0; k < rhs.theTrees.length; k++)
            rhs.theTrees[k] = null;//�ϲ����֮��,�ͷ�rhs�ڴ�
        rhs.currentSize = 0;
    }

    /**
     * Insert into the priority queue, maintaining heap order.
     * This implementation is not optimized for O(1) performance.
     *
     * @param x the item to insert.
     */
    public void insert(AnyType x) {
        merge(new BinomialQueue<>(x));
    }

    /**
     * Remove the smallest item from the priority queue.
     *
     * @return the smallest item, or throw UnderflowException if empty.
     */
    public AnyType deleteMin() {
        if (isEmpty())
            throw new UnderflowException();

        int minIndex = findMinIndex();
        AnyType minItem = theTrees[minIndex].element;

        BinNode<AnyType> deletedTree = theTrees[minIndex].leftChild;

        // Construct H''
        BinomialQueue<AnyType> deletedQueue = new BinomialQueue<>();
        deletedQueue.expandTheTrees(minIndex + 1);

        deletedQueue.currentSize = (1 << minIndex) - 1;
        for (int j = minIndex - 1; j >= 0; j--) {
            deletedQueue.theTrees[j] = deletedTree;
            deletedTree = deletedTree.nextSibling;
            deletedQueue.theTrees[j].nextSibling = null;
        }

        // Construct H'
        theTrees[minIndex] = null;
        currentSize -= deletedQueue.currentSize + 1;

        merge(deletedQueue);

        return minItem;
    }

    private Integer capacity(){
        return null;
    }

    private Boolean isEmpty(){
        return null;
    }
    private Boolean makeEmpty(){
        return null;
    }

}