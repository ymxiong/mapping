package cc.eamon.open.mapping.mapper.util;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-07-29 02:58:26
 */
public class StringUtils {

    public static String firstWordToUpperCase(String input){
        if (input == null){
            return null;
        }else if (input.length() == 0){
            return "";
        }else {
            return (input.charAt(0) + "").toUpperCase() + input.substring(1);
        }
    }

    public static String firstWordToLowerCase(String input){
        if (input == null){
            return null;
        }else if (input.length() == 0){
            return "";
        }else {
            return (input.charAt(0) + "").toLowerCase() + input.substring(1);
        }
    }

    public static String packageNameFromQualifiedName(String qualifiedName){
        if (qualifiedName == null){
            return null;
        }
        return qualifiedName.replaceAll("\\.[^.]+$", "");
    }

    public static String classNameFromQualifiedName(String qualifiedName){
        if (qualifiedName == null){
            return null;
        }
        return qualifiedName.replaceAll(".*\\.", "");
    }

}
