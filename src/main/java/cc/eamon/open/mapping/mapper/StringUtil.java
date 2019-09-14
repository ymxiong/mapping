package cc.eamon.open.mapping.mapper;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-07-29 02:58:26
 */
public class StringUtil {

    public static String firstWordToUpperCase(String input){
        if (input == null){
            return null;
        }else if (input.length() == 0){
            return "";
        }else {
            return (input.charAt(0) + "").toUpperCase() + input.substring(1);
        }
    }

}
