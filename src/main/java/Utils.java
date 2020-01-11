import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    /**
     * Question1, sort by firstName + lastName + ext, if firstName is the same then sort by lastName and ext,
     * please note lastName and ext
     * can be empty string or null.
     **/
    //没看明白String类型怎么排序，firstName的首字母，还是firstName的长度（我只能按照length先来），也没讲是asc还是desc
    //如果用工具会很简单
    // Collections.sort(list, new Comparator<Extension>() {
    //    public int compare(Extension s1, Extension s2) {
    //        if(s1.getFirstName()-(s2.getFirstName())!=0){
    //            return s1.getFirstName()-(s2.getFirstName());
    //        }else   if(s1.getFirstName()-(s2.getFirstName())!=0){
    //            return  s1.getLastName().compareTo(s2.getLastName());
    //        }else{
    //            return  s1.ext().compareTo(s2.ext());
    //        }
    //    }
    //});
    //  这个排序底层算法用户插入排序  ,下面使用了快排
    public static List<Extension> sortByName(List<Extension> extensions) {
        //快排
        quickSort(extensions.toArray(new Extension[extensions.size()]), 0, extensions.size() - 1);
        return extensions;
    }

    /**
     * Question2, sort extType, extType is a string and can be "User", "Dept", "AO", "TMO", "Other", sort by User > Dept > AO > AO > Other;
     **/
    public static List<Extension> sortByExtType(List<Extension> extensions) {

        //计算方式插排，快排，中间排序，冒泡  这里只使用了插排(每种排序效率不一样，不过十万以内看不出什么差别)
        //如果超过湿完了，就要换思路了：比如引入分布式
        List<Extension> list = new ArrayList<Extension>();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("user", 1);
        map.put("Dept", 2);
        map.put("AO", 3);
        map.put("usAOer", 4);
        map.put("Other", 5);
        int low = 0;
        int high = extensions.size();
        for (int i = low; i < high; i++) {
            for (int j = i; j > low && !compare(map.get(list.get(j).getExtType()), map.get(list.get(j - 1).getExtType())); j--) {
                swap(extensions, j, j - 1);
            }
        }
        return extensions;
    }

    /**
     * Question3, sum all sales items by quarter
     * 按季度汇总所有销售项目，求出每个季度的total
     **/
    public static List<QuarterSalesItem> sumByQuarter(List<SaleItem> saleItems) {
        List<QuarterSalesItem> list = new ArrayList<QuarterSalesItem>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date oneQuartStart = df.parse(new String("2018-01-01"));
            Date oneQuartEnd = df.parse(new String("2018-03-30"));

            Date twoQuartStart = df.parse(new String("2018-04-01"));
            Date twoQuartEnd = df.parse(new String("2018-06-30"));

            Date threeQuartStart = df.parse(new String("2018-07-01"));
            Date threeQuartEnd = df.parse(new String("2018-09-30"));

            Date fourQuartStart = df.parse(new String("2018-10-01"));
            Date fourQuartEnd = df.parse(new String("2018-12-30"));

            double firstQut = 0;
            double twoQut = 0;
            double threeQut = 0;
            double fourQut = 0;

            //for循环里的逻辑用stream处理会更好
            for (Iterator<SaleItem> i = saleItems.iterator(); i.hasNext(); ) {//时间复杂度O（n）
                SaleItem item = i.next();
                while (item.getDate().after(oneQuartStart) && item.getDate().before(oneQuartEnd) && item.getDate().equals(oneQuartEnd)) {
                    firstQut = item.getSaleNumbers() + firstQut;
                    continue;
                }
                while (item.getDate().after(twoQuartStart) && item.getDate().before(twoQuartEnd) && item.getDate().equals(twoQuartEnd)) {
                    twoQut = item.getSaleNumbers() + twoQut;
                    continue;
                }
                while (item.getDate().after(threeQuartStart) && item.getDate().before(threeQuartEnd) && item.getDate().equals(threeQuartEnd)) {
                    threeQut = item.getSaleNumbers() + threeQut;
                    continue;
                }
                while (item.getDate().after(fourQuartStart) && item.getDate().before(fourQuartEnd) && item.getDate().equals(fourQuartEnd)) {
                    fourQut = item.getSaleNumbers() + fourQut;
                    continue;
                }
            }
            QuarterSalesItem firstitem = new QuarterSalesItem();
            firstitem.setQuarter(1);
            firstitem.setTotal(firstQut);
            list.add(firstitem);
            QuarterSalesItem twoitem = new QuarterSalesItem();
            twoitem.setQuarter(2);
            twoitem.setTotal(twoQut);
            list.add(twoitem);

            QuarterSalesItem threeitem = new QuarterSalesItem();
            threeitem.setQuarter(3);
            threeitem.setTotal(threeQut);
            list.add(threeitem);

            QuarterSalesItem fouritem = new QuarterSalesItem();
            fouritem.setQuarter(4);
            fouritem.setTotal(fourQut);
            list.add(fouritem);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Question4, max all sales items by quarter
     * 求出每个季度的最大值，这里使用了stream，但是流的效率貌似并不高，还是自己写的算法效率更高，关键看数据量有多大
     **/
    public List<QuarterSalesItem> maxByQuarter(List<SaleItem> saleItems) {
        List<QuarterSalesItem> list = new ArrayList<QuarterSalesItem>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date oneQuartStart = df.parse(new String("2018-01-01"));
            Date oneQuartEnd = df.parse(new String("2018-03-30"));

            Date twoQuartStart = df.parse(new String("2018-04-01"));
            Date twoQuartEnd = df.parse(new String("2018-06-30"));

            Date threeQuartStart = df.parse(new String("2018-07-01"));
            Date threeQuartEnd = df.parse(new String("2018-09-30"));

            Date fourQuartStart = df.parse(new String("2018-10-01"));
            Date fourQuartEnd = df.parse(new String("2018-12-30"));

            List<SaleItem> saleItemsFirst = new ArrayList<SaleItem>();
            List<SaleItem> saleItemsTwo = new ArrayList<SaleItem>();
            List<SaleItem> saleItemsThree = new ArrayList<SaleItem>();
            List<SaleItem> saleItemsFour = new ArrayList<SaleItem>();

            for (Iterator<SaleItem> i = saleItems.iterator(); i.hasNext(); ) {
                SaleItem item = i.next();
                while (item.getDate().after(oneQuartStart) && item.getDate().before(oneQuartEnd) && item.getDate().equals(oneQuartEnd)) {
                    saleItemsFirst.add(item);
                    continue;
                }
                while (item.getDate().after(twoQuartStart) && item.getDate().before(twoQuartEnd) && item.getDate().equals(twoQuartEnd)) {
                    saleItemsTwo.add(item);

                    continue;
                }
                while (item.getDate().after(threeQuartStart) && item.getDate().before(threeQuartEnd) && item.getDate().equals(threeQuartEnd)) {
                    saleItemsThree.add(item);

                    continue;
                }
                while (item.getDate().after(fourQuartStart) && item.getDate().before(fourQuartEnd) && item.getDate().equals(fourQuartEnd)) {
                    saleItemsFour.add(item);

                    continue;
                }
            }
            OptionalDouble fist = saleItemsFirst.stream().mapToDouble(SaleItem::getSaleNumbers).max();//最大
            QuarterSalesItem firstitem = new QuarterSalesItem();
            firstitem.setQuarter(1);
            firstitem.setTotal(fist.getAsDouble());
            list.add(firstitem);


            OptionalDouble two = saleItemsTwo.stream().mapToDouble(SaleItem::getSaleNumbers).max();//最大
            QuarterSalesItem twoitem = new QuarterSalesItem();
            twoitem.setQuarter(2);
            twoitem.setTotal(two.getAsDouble());
            list.add(twoitem);

            OptionalDouble threeQut = saleItemsThree.stream().mapToDouble(SaleItem::getSaleNumbers).max();//最大

            QuarterSalesItem threeitem = new QuarterSalesItem();
            threeitem.setQuarter(3);
            threeitem.setTotal(threeQut.getAsDouble());
            list.add(threeitem);

            OptionalDouble fourQut = saleItemsFour.stream().mapToDouble(SaleItem::getSaleNumbers).max();//最大
            QuarterSalesItem fouritem = new QuarterSalesItem();
            fouritem.setQuarter(4);
            fouritem.setTotal(fourQut.getAsDouble());
            list.add(fouritem);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * We have all Keys: 0-9; usedKeys is an array to store all used keys like : [2,3,4]; We want to get all unused keys, in this example it
     * would be: [0,1,5,6,7,8,9,]
     */

    public static int[] getUnUsedKeys(int[] allKeys, int[] usedKeys) {

        //first 冒泡
        int[] temp = new int[allKeys.length];
        int y = 0;
        for (int i = 0; i < allKeys.length; i++) {
            int x = allKeys[i];
            for (int j = 0; j < usedKeys.length; j++) {
                if (usedKeys[j] == x) {
                    continue;
                }
                temp[y] = allKeys[i];
                y++;
            }
        }

        //two 插排
        //for (int i = 0; i < allKeys.length; i++) {
        //    int x = allKeys[i];
        //    for(int j = 0;j<i;j--){
        //        if (usedKeys[j] == x) {
        //            continue;
        //        }
        //        temp[y] = allKeys[i];
        //        y++;
        //    }
        //}

        //three
        return temp;
    }


    /**
     * Swaps x[a] with x[b].
     */
    private static void swap(Extension[] x, int a, int b) {
        Extension t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    /**
     * Swaps x[a] with x[b].
     */
    private static void swap(List<Extension> extensions, int a, int b) {
        extensions.add(b, extensions.get(a));
        extensions.add(a, extensions.get(b));
    }

    /**
     * Swaps x[a] with x[b].
     */
    private static boolean compare(Integer obj, Integer obj1) {
        if (obj > obj1) {
            return true;
        }
        return false;
    }

    private static void quickSort(Extension[] arr, int low, int high) {
        if (low < high) {
            // 找寻基准数据的正确索引
            int index = getIndex(arr, low, high);

            // 进行迭代对index之前和之后的数组进行相同的操作使整个数组变成有序
            quickSort(arr, 0, index - 1);
            quickSort(arr, index + 1, high);
        }
    }

    private static int getIndex(Extension[] arr, int low, int high) {
        // 基准数据
        Extension tmp = arr[low];
        while (arr[low].getFirstName().length() < arr[high].getFirstName().length()) {
            // 当队尾的元素大于等于基准数据时,向前挪动high指针
            while (arr[low].getFirstName().length() < arr[high].getFirstName().length()
                    && arr[high].getFirstName().length() >= tmp.getFirstName().length()) {
                if (arr[high].getFirstName().length() == tmp.getFirstName().length()) {  //如果性相等，判断名，如果名相等，判断ext
                    if (arr[high].getLastName().length() > tmp.getLastName().length()) {
                        swap(arr, high, low);
                    }
                    if (arr[high].getLastName().length() == tmp.getLastName().length()) {
                        if (arr[high].getExt().length() > tmp.getExt().length()) {
                            swap(arr, high, low);
                        }
                    }
                }
                high--;
            }
            // 如果队尾元素小于tmp了,需要将其赋值给low
            arr[low] = arr[high];
            // 当队首元素小于等于tmp时,向前挪动low指针
            while (arr[low].getFirstName().length() < arr[high].getFirstName().length()
                    && arr[low].getFirstName().length() <= tmp.getFirstName().length()) {
                if (arr[high].getFirstName().length() == tmp.getFirstName().length()) {
                    if (arr[high].getLastName().length() < tmp.getLastName().length()) {
                        swap(arr, high, low);
                    }
                    if (arr[high].getLastName().length() == tmp.getLastName().length()) {
                        if (arr[high].getExt().length() < tmp.getExt().length()) {
                            swap(arr, high, low);
                        }
                    }
                }
                low++;
            }
            // 当队首元素大于tmp时,需要将其赋值给high
            arr[high] = arr[low];
        }
        // 跳出循环时low和high相等,此时的low或high就是tmp的正确索引位置
        // 由原理部分可以很清楚的知道low位置的值并不是tmp,所以需要将tmp赋值给arr[low]
        arr[low] = tmp;
        return low; // 返回tmp的正确位置
    }
}
