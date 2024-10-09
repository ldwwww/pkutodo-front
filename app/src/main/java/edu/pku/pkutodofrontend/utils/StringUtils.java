package edu.pku.pkutodofrontend.utils;

import org.json.JSONObject;


/**
 * 字符串解处理工具类
 */
public class StringUtils {

    /**
     * 从json字符串中提取key
     * @param jsonString
     * @param key
     * @return String
     */

    public static String extractString(String jsonString, String key) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
