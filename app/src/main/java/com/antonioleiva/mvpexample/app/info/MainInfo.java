package com.antonioleiva.mvpexample.app.info;


import java.io.Serializable;

public class MainInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    // vSQLite version : tang them khi co thay doi cot  trong 1 tabl
    /**
     * Thiết lập chế độ chạy (kết nối đến local, test, demo hay real).
     * Mỗi chế độ sẽ có những chức năng, cách chạy khác nhau...
     */
    public static final String MethodDemo = "v1/serviceProvider/register";
    public static String url_server = "http://52.89.66.217:8181/api/";

}
