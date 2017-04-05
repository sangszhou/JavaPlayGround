package base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xinszhou on 03/04/2017.
 */
public class NumberUtil {

    public static int retrieveIntFromString(String content) {
        Pattern p = Pattern.compile("-?\\d+(,\\d+)*?\\.?\\d+?");
        Matcher m = p.matcher(content);

        if (m.find()) {
            return Integer.parseInt(m.group());
        } else {
            return -121323435;
        }



    }
}
