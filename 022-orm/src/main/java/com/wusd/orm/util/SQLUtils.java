package com.wusd.orm.util;

import java.util.ArrayList;
import java.util.List;

public class SQLUtils {
    public static String[] sqlInsertParameter(String SQL) {
        int startIndex = SQL.indexOf("values");
        int endIndex = SQL.length();
        String substring = SQL.substring(startIndex + 6, endIndex)
                .replace(")", "")
                .replace("#{", "")
                .replace("}", "");
        String[] split = substring.split(",");
        return split;
    }
    public static List<String> sqlSelectParamter(String SQL) {
        int startIndex = SQL.indexOf("where");
        int endIndex = SQL.length();
        String substring = SQL.substring(startIndex + 5, endIndex);
        String[] split = substring.split("and");
        List<String> listArr = new ArrayList<>();
        for (String string : split) {
            String[] sp2 = string.split("=");
            listArr.add(sp2[0].trim());
        }
        return listArr;
    }
    public static String paramQuestion(String SQL, String[] parameterName) {
        for (int i = 0; i < parameterName.length; i++) {
            String string = parameterName[i];
            SQL = SQL.replace("#{" + string + "}", "?");
        }
        return SQL;
    }

    public static String paramQuestion(String SQL, List<String> parameterName) {
        for (int i = 0; i < parameterName.size(); i++) {
            String string = parameterName.get(i);
            SQL = SQL.replace("#{" + string + "}", "?");
        }
        return SQL;
    }
}
