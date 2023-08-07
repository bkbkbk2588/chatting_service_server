package com.example.chatting_server.util;

public enum ResponseCode {
    SUCCESS(200, "SUCCESS", "SUCCESS"),
    NO_DATA(201, "NO_DATA", "NO_DATA"),
    NO_CONTENT(204, "NO_CONTENT", "No Content"),
    FAIL(500, "FAIL", "FAIL"),

    // API : 1000
    NO_REQUIRED_PARAM(1001, "NO_REQUIRED_PARAM", "No Required Parameter"),
    INVALID_PARAM_TYPE(1002, "INVALID_PARAM_TYPE", "Invalid Parameter Type"),
    INVALID_PARAM_LENGTH(1003, "INVALID_PARAM_LENGTH", "Invalid Parameter Length"),
    INVALID_PARAM_VALUE(1004, "INVALID_PARAM_VALUE", "Invalid Parameter Value"),

    // USER : 2000
    ALREADY_EXIST_ID(2000, "ALREADY_EXIST_ID", "Already Exist Id"),


    // DB : 7000
    DB_COMMIT_FAIL(7000, "DB_COMMIT_FAIL", "Db Commit Fail");

    private final int code;
    private final String codeName;
    private final String message;

    ResponseCode(int code, String codeName, String message){
        this.code = code;
        this.codeName = codeName;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getMessage() {
        return message;
    }
}
