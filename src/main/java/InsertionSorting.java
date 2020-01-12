public class InsertionSorting {
    // 插入排序
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int arr[]={12,5,17,16,19,23,14,52,13};

        for(int i=1;i<arr.length;i++){
            int insertVal=arr[i];
            //insertVal准备和前一个数比较

            int index=i-1;
            while(index>=0 && insertVal<arr[index]){
                //将arr[index]向后移动一位
                arr[index+1]=arr[index];
                index--;
            }
            //将insertVal的值插入适当位置
            arr[index+1]=insertVal;
        }
        //输出结果
        for(int e:arr){
            System.out.print(e+" ");
        }

    }

}
