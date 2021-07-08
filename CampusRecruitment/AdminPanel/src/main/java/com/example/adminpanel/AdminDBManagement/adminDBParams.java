package com.example.adminpanel.AdminDBManagement;

public class adminDBParams {

    public static final String ADMIN_TBL = "Admin_Credentials";
    public static final String ADMIN_ID = "ID";
    public static final String ADMIN_NAME = "name";
    public static final String ADMIN_PASS = "Password";

    public static final String CreateAdminTable = "CREATE TABLE "+ADMIN_TBL+"("+
            ADMIN_ID+" VARCHAR(30) PRIMARY KEY,"+
            ADMIN_NAME+" VARCHAR(50) NOT NULL,"+
            ADMIN_PASS+" VARCHAR(16) NOT NULL);";
}
