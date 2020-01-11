import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    /**
     * Question1, sort by firstName + lastName + ext, if firstName is the same then sort by lastName and ext,
     * please note lastName and ext
     * can be empty string or null.
     **/
    //û������String������ô����firstName������ĸ������firstName�ĳ��ȣ���ֻ�ܰ���length��������Ҳû����asc����desc
    //����ù��߻�ܼ�
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
    //  �������ײ��㷨�û���������  ,����ʹ���˿���
    public static List<Extension> sortByName(List<Extension> extensions) {
        //����
        quickSort(extensions.toArray(new Extension[extensions.size()]), 0, extensions.size() - 1);
        return extensions;
    }

    /**
     * Question2, sort extType, extType is a string and can be "User", "Dept", "AO", "TMO", "Other", sort by User > Dept > AO > AO > Other;
     **/
    public static List<Extension> sortByExtType(List<Extension> extensions) {

        //���㷽ʽ���ţ����ţ��м�����ð��  ����ֻʹ���˲���(ÿ������Ч�ʲ�һ��������ʮ�����ڿ�����ʲô���)
        //�������ʪ���ˣ���Ҫ��˼·�ˣ���������ֲ�ʽ
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
     * �����Ȼ�������������Ŀ�����ÿ�����ȵ�total
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

            //forѭ������߼���stream��������
            for (Iterator<SaleItem> i = saleItems.iterator(); i.hasNext(); ) {//ʱ�临�Ӷ�O��n��
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
     * ���ÿ�����ȵ����ֵ������ʹ����stream����������Ч��ò�Ʋ����ߣ������Լ�д���㷨Ч�ʸ��ߣ��ؼ����������ж��
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
            OptionalDouble fist = saleItemsFirst.stream().mapToDouble(SaleItem::getSaleNumbers).max();//���
            QuarterSalesItem firstitem = new QuarterSalesItem();
            firstitem.setQuarter(1);
            firstitem.setTotal(fist.getAsDouble());
            list.add(firstitem);


            OptionalDouble two = saleItemsTwo.stream().mapToDouble(SaleItem::getSaleNumbers).max();//���
            QuarterSalesItem twoitem = new QuarterSalesItem();
            twoitem.setQuarter(2);
            twoitem.setTotal(two.getAsDouble());
            list.add(twoitem);

            OptionalDouble threeQut = saleItemsThree.stream().mapToDouble(SaleItem::getSaleNumbers).max();//���

            QuarterSalesItem threeitem = new QuarterSalesItem();
            threeitem.setQuarter(3);
            threeitem.setTotal(threeQut.getAsDouble());
            list.add(threeitem);

            OptionalDouble fourQut = saleItemsFour.stream().mapToDouble(SaleItem::getSaleNumbers).max();//���
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

        //first ð��
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

        //two ����
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
            // ��Ѱ��׼���ݵ���ȷ����
            int index = getIndex(arr, low, high);

            // ���е�����index֮ǰ��֮������������ͬ�Ĳ���ʹ��������������
            quickSort(arr, 0, index - 1);
            quickSort(arr, index + 1, high);
        }
    }

    private static int getIndex(Extension[] arr, int low, int high) {
        // ��׼����
        Extension tmp = arr[low];
        while (arr[low].getFirstName().length() < arr[high].getFirstName().length()) {
            // ����β��Ԫ�ش��ڵ��ڻ�׼����ʱ,��ǰŲ��highָ��
            while (arr[low].getFirstName().length() < arr[high].getFirstName().length()
                    && arr[high].getFirstName().length() >= tmp.getFirstName().length()) {
                if (arr[high].getFirstName().length() == tmp.getFirstName().length()) {  //�������ȣ��ж������������ȣ��ж�ext
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
            // �����βԪ��С��tmp��,��Ҫ���丳ֵ��low
            arr[low] = arr[high];
            // ������Ԫ��С�ڵ���tmpʱ,��ǰŲ��lowָ��
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
            // ������Ԫ�ش���tmpʱ,��Ҫ���丳ֵ��high
            arr[high] = arr[low];
        }
        // ����ѭ��ʱlow��high���,��ʱ��low��high����tmp����ȷ����λ��
        // ��ԭ���ֿ��Ժ������֪��lowλ�õ�ֵ������tmp,������Ҫ��tmp��ֵ��arr[low]
        arr[low] = tmp;
        return low; // ����tmp����ȷλ��
    }
}
