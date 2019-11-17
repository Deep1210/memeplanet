package in.incourt.incourtnews.helpers;

/**
 * Created by bhavan on 3/1/17.
 */

public class IncourtStringHelper {

    public static String capitalize(String input) {
        if (input == null || input.length() <= 0) {
            return input;
        }
        char[] chars = new char[1];
        input.getChars(0, 1, chars, 0);
        if (Character.isUpperCase(chars[0])) {
            return input;
        } else {
            StringBuilder buffer = new StringBuilder(input.length());
            buffer.append(Character.toUpperCase(chars[0]));
            buffer.append(input.toCharArray(), 1, input.length()-1);
            return buffer.toString();
        }
    }
}
