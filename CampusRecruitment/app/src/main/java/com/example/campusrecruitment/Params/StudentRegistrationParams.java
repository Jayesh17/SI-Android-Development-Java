package com.example.campusrecruitment.Params;

public class StudentRegistrationParams {

    public static final String REGISTER_STUDENTS_TBL = "Student_Registration";
    public static final String EMAIL_ID = "Email";
    public static final String SNAME = "Name";
    public static final String PASSWORD = "Password";

    public static final String CREATE_QUERY = "" +
            "CREATE TABLE "+REGISTER_STUDENTS_TBL+"("+
            EMAIL_ID+" VARCHAR(30) PRIMARY KEY,"+
            SNAME+" VARCHAR(50) NOT NULL,"+
            PASSWORD+" VARCHAR(16) NOT NULL);";

    public static enum status{
        NOT_EXIST,
        WRONG_PASS,
        ERROR,
        DONE
    }

}
