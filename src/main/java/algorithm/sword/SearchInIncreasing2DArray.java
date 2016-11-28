package algorithm.sword;

/**
 * Created by xinszhou on 28/11/2016.
 */
public class SearchInIncreasing2DArray {
    public boolean Find(int target, int [][] array) {
        if(array.length <= 0 || array[0].length <= 0) return false;
        int x = 0, y = array[0].length;

        while(x < array.length && y >= 0) {
            int temp = array[x][y];
            if(temp == target) return true;

            if(temp > target)
                x += 1;
            else
                y -= 1;
        }

        return false;
    }

}
