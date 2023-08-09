package com.example.chatting_server.util;

public enum ResponseCode {
    SUCCESS(200, "SUCCESS", "SUCCESS"),
    NO_DATA(201, "NO_DATA", "NO_DATA"),
    NO_CONTENT(204, "NO_CONTENT", "No Content"),
    UNAUTHORIZED(401, "Unauthorized", "Unauthorized"),
    FORBIDDEN(403, "Forbidden", "Forbidden"),
    FAIL(500, "FAIL", "FAIL"),

    // API : 1000
    NO_REQUIRED_PARAM(1001, "NO_REQUIRED_PARAM", "No Required Parameter"),
    INVALID_PARAM_TYPE(1002, "INVALID_PARAM_TYPE", "Invalid Parameter Type"),
    INVALID_PARAM_LENGTH(1003, "INVALID_PARAM_LENGTH", "Invalid Parameter Length"),
    INVALID_PARAM_VALUE(1004, "INVALID_PARAM_VALUE", "Invalid Parameter Value"),

    // USER : 2000
    ALREADY_EXIST_ID(2000, "ALREADY_EXIST_ID", "Already Exist Id"),
    NO_EXIST_USER_ID(2001, "NO_EXIST_USER_ID", "No Exist User Id"),
    NO_EXIST_USER(2002, "NO_EXIST_USER", "No Exist User"),
    INCORRECT_ID_OR_PASSWORD(2003, "INCORRECT_ID_OR_PASSWORD", "ID or Password is incorrect"),

    // TOKEN : 3000
    INVALID_REFRESH_TOKEN(3000, "INVALID_REFRESH_TOKEN", "Invalid Refresh Token"),
    INVALID_ACCESS_TOKEN(3001, "INVALID_ACCESS_TOKEN", "Invalid Access Token"),

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
