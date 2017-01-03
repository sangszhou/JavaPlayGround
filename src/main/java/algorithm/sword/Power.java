package algorithm.sword;

/**
 * Created by xinszhou on 04/12/2016.
 */
public class Power {

    public double Power(double base, int exponent) {
        if(exponent > 0) return PositivePower(base, exponent);
        else return 1 / PositivePower(base, exponent * -1);
    }

    public double PositivePower(double base, int exponent) {
        if (exponent == 0) {
            return 1;
        } else if (exponent == 1) {
            return base;
        }

        double half = Power(base, exponent / 2);

        if (exponent % 2 == 0) {
            return half * half;
        } else {
            return half * half * base;
        }
    }

}
