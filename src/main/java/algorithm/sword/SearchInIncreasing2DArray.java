package algorithm.sword;

/**
 * Created by xinszhou on 28/11/2016.
 */
public class SearchInIncreasing2DArray {

    public boolean Find(int target, int [][] array) {
        if(array == null) return false;
        if(array.length <= 0 || array[0].length <= 0) return false;

        int x = 0, y = array[0].length-1;

        while(x < array.length && y >= 0) {
            int temp = array[x][y];
            if(temp == target)
                return true;
            else if(temp > target)
                y -= 1;
            else
                x += 1;
        }
        return false;
    }

    public static void main(String args[]) {
//        7,[[1,2,8,9],[2,4,9,12],[4,7,10,13],[6,8,11,15]]
        int target = 7;

    }

}
