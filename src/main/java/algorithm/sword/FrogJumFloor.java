package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class FrogJumFloor {

    public int JumpFloor(int target) {
        // f(n) = f(n-1) + f(n-2)

        if(target == 0) return 0;
        if(target == 1) return 1;
        if(target == 2) return 2;

        int last = 2;
        int lastlast = 1;

        int result = 0;
        for (int i = 3; i <= target; i++) {
            result = last + lastlast;
            lastlast = last;
            last = result;
        }
        return result;
    }

    public int JumpFloorII(int target) {
        if(target == 0) return 0;
        if(target == 1) return 1;
        if(target == 2) return 2;

        // f(n) = f(n-1) + f(n-2) + f(n-3) ...
        // 1, 1, 2, 4, 8 // special when target is 0

        return (int) Math.pow(2, target - 1);
    }

    public static void main(String args[]) {
        FrogJumFloor frog = new FrogJumFloor();
        int result = frog.JumpFloorII(4);
        System.out.println(result);
    }

}
