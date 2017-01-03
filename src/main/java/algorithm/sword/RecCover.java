package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class RecCover {
    public int RectCover(int target) {
        if(target == 0) return 0;
        if(target == 1) return 1;
        if(target == 2) return 2; // 横竖各放一个

        return RectCover(target - 1) + RectCover(target - 2);
    }

}
