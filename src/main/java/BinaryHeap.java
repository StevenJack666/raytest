import java.util.ArrayList;

/**
 * 头元素存储于1位置 i节点的左儿子位置2*i 右儿子位置2*i+1   父亲位置i/2
 */
public class BinaryHeap<T extends Comparable<T>> {


    /**
     * 由于java禁止使用泛型数组，故此处使用ArrayList存储信息
     */
    private ArrayList<T> array;//
    private int currentSize;//大小

    public BinaryHeap() {
        array = new ArrayList<T>();
        array.add(null);
    }

    /**
     * 将数组转化为二叉堆
     *
     * @param array
     */
    public BinaryHeap(ArrayList<T> array) {
        if(this.array==null){
            this.array = new ArrayList<T>();
            this.array.add(null);
        }
        currentSize = array.size();
        int i = 1;
        for (T t : array) {//保证结构性
            this.array.add(i++, t);
        }
        //保证堆性 下滤
        for (i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    /**
     * 插入
     *
     * @param x
     */
    public void insert(T x) {
        int hole = currentSize + 1;//空穴的初始位置
        array.add(x);
        //当x元素小于空穴的父节点时，空穴进行上滤
        for (; hole > 1 && x.compareTo(array.get(hole / 2)) < 0; hole = hole / 2) {
            array.set(hole, array.get(hole / 2));
        }
        //当x元素不小于空穴的父节点元素时，找到合适的位置，放入
        array.set(hole, x);
        currentSize++;
    }

    /**
     * 查找最小元素
     *
     * @return
     */
    public T findMin() {
        return array.get(1);
    }

    /**
     * 删除最小元素
     *
     * @return
     */
    public T deleteMin() {
        if (currentSize < 1) {
            System.out.println("BinaryHeap is Empty");
        }
        T minElement = array.get(1);//获取最小元素
        array.set(1, array.get(currentSize));//将末尾元素存入
        array.remove(currentSize--);//移除最后元素，并使大小-1
        if (currentSize > 0) {
            percolateDown(1);//下滤
        }
        return minElement;
    }

    /**
     * 删除任一元素
     *
     * @return 元素不存在，返回-1 删除成功，返回该元素的下标
     */
    public int delete(T x) throws Exception {
        if (currentSize < 1) {
            throw new Exception("BinaryHeap is Empty");
        }
        int index = array.indexOf(x);//获取x的索引
        if (index == -1) {
            return -1;
        }
        array.set(index, array.get(currentSize));
        array.remove(currentSize--);
        percolateDown(index);//下滤
        return index;
    }

    /**
     * 下滤
     */
    public void percolateDown(int hole) {
        int child;
        T temp = array.get(hole);//需要下滤的元素，临时存储
        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            //child不为最后一个元素，且右元素小于左元素时：child为右元素，否则child为左元素
            if (child < currentSize && array.get(child).compareTo(array.get(child + 1)) > 0) {
                child++;
            }
            if (temp.compareTo(array.get(child)) > 0) {//temp大于较小的元素 ，将空穴下滤一层
                array.set(hole, array.get(child));
            } else {
                break;//找到合适的位置，跳出循环替换
            }
        }
        array.set(hole, temp);
    }

    public static void main(String[] args) {

        ArrayList<Integer> array = new ArrayList<>();
        array.add(10);
        array.add(11);
        array.add(18);
        array.add(3);
        array.add(7);
//        array.add(20);
        array.add(6);

        BinaryHeap<Integer> hs = new BinaryHeap<Integer>(array);
        System.out.println(hs.array);

        int numItems = 1000;
        BinaryHeap<Integer> h = new BinaryHeap<Integer>();
        int i = 37;
        for (i = 37; i != 0; i = (i + 37) % numItems) {
            h.insert(i);
        }

        for (i = 1; i < numItems; i++) {
            if (h.deleteMin() != i) {

                System.out.println("Oops! " + i);
            }
        }

    }
}


