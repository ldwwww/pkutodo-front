package edu.pku.pkutodofrontend.utils;

/**
 * API接口地址
 */
public class APIUtils {
    public static final String BASE_URL = "http://10.7.168.23:8080"; // 换成本机IP或者localhost
    public static final String TEST = BASE_URL + "/test/hello";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String REGISTER = BASE_URL + "/user/register";
    public static final String SEND_EMAIL = BASE_URL + "/user/sendEmail";
}
