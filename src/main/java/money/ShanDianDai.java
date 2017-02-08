package money;

/**
 * Created by xinszhou on 02/02/2017.
 */
public class ShanDianDai {

    public static void main(String args[]) {

        double monthlyPayment = 3404;
        double yearlyInterest = 0.085;
        double totalMoney = 300000;

        // 10 年后我还能剩多少钱
        for (int i = 0; i < 120; i++) {
            totalMoney = totalMoney * (1 + yearlyInterest / 12);
            totalMoney = totalMoney - monthlyPayment;
            totalMoney = new Double(totalMoney).intValue();

            if( (i+1) % 12 == 0) {
                System.out.println("第 " + ((i + 1)/12) + " 年后，还剩 " + totalMoney + " 元");
            }
        }
    }
}
