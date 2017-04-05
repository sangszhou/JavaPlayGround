package base.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xinszhou on 03/04/2017.
 */
public class NumberUtilTest {
    @Test
    public void retrieveIntFromString() throws Exception {
        int number = NumberUtil.retrieveIntFromString("323楼房");
        System.out.println(number);

        int number2 = NumberUtil.retrieveIntFromString("123户数");
        System.out.println(number2);

    }

}