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
    UPDATE_USER_FAIL(2004, "UPDATE_USER_FAIL", "Update User Fail"),
    NO_EXIST_USER_METADATA(2005, "NO_EXIST_USER_METADATA", "No Exist User Metadata"),
    ALREADY_EXIST_NICKNAME(2006, "ALREADY_EXIST_NICKNAME", "Already Exist Nickname"),
    NO_EXIST_REQUEST_FRIEND(2007, "NO_EXIST_REQUEST_FRIEND", "No Exist Request Friend"),
    UNAUTHORIZED_REQUEST_FRIEND(2008, "UNAUTHORIZED_REQUEST_FRIEND", "Unauthorized Request Friend"),
    ALREADY_EXIST_REQUEST_FRIEND(2009, "ALREADY_EXIST_REQUEST_FRIEND", "Already Exist Request Friend"),
    NO_EXIST_FRIEND(2010, "NO_EXIST_FRIEND", "No Exist Friend"),
    UNAUTHORIZED_FRIEND(2011, "UNAUTHORIZED_FRIEND", "Unauthorized Friend"),
    NO_ME_REQUEST_FRIEND(2012, "NO_ME_REQUEST_FRIEND", "No me Request Friend"),

    // TOKEN : 3000
    INVALID_REFRESH_TOKEN(3000, "INVALID_REFRESH_TOKEN", "Invalid Refresh Token"),
    INVALID_ACCESS_TOKEN(3001, "INVALID_ACCESS_TOKEN", "Invalid Access Token"),

    // CHANNEL : 4000
    NO_EXIST_CHANNEL(4000, "NO_EXIST_CHANNEL", "No Exist Channel"),
    UNAUTHORIZED_CHANNEL(4001, "UNAUTHORIZED_CHANNEL", "Unauthorized Channel"),
    NO_EXIST_CHANNEL_USER(4002, "NO_EXIST_CHANNEL_USER", "No Exist Channel User"),
    NO_EXIST_INVITE_CHANNEL_USER(4003, "NO_EXIST_INVITE_CHANNEL_USER", "No Exist Invite Channel User"),
    EXIST_INVITE_CHANNEL_USER(4004, "EXIST_INVITE_CHANNEL_USER", "Exist Invite Channel User"),
    NO_EXIST_INVITE_CHANNEL(4005, "NO_EXIST_INVITE_CHANNEL", "No Exist Invite Channel"),
    UNAUTHORIZED_INVITE_CHANNEL_USER(4006, "UNAUTHORIZED_INVITE_CHANNEL_USER", "Unauthorized Invite Channel User"),
    EXIST_CHANNEL_METADATA(4007, "EXIST_CHANNEL_METADATA", "Exist Channel Metadata"),
    NO_EXIST_CHANNEL_METADATA(4008, "NO_EXIST_CHANNEL_METADATA", "No Exist Channel Metadata"),

    // DB : 7000
    DB_COMMIT_FAIL(7000, "DB_COMMIT_FAIL", "Db Commit Fail"),

    // ETC : 8000
    JSON_PARSE_ERROR(8000, "JSON_PARSE_ERROR", "Json Parse Error");

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
