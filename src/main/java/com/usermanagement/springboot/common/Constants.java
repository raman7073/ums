package com.usermanagement.springboot.common;

public class Constants {

    public static final String LOGIN_API = "/v1/auth/login";
    public static final String V1_AUTH = "/v1/auth";
    public static final String V1_USERS = "/v1/users";
    public static final String USERS = "users";
    public static final String USER = "User";
    public static final String USERNAME = "username";
    public static final String LOGIN = "/login";
    public static final String VAR_USERID = "/{userId}";
    public static final String ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String USERID = "userId";
    public static final String UPDATED = "UPDATED_SUCCESSFULLY";
    public static final String CHANGEPASSWORD = "/change-password";
    public static final String FINDALLQUERY = "SELECT * FROM users u WHERE u.deleted = false";
    public static final String SQL_ON_DELETE_QUERY =
            "UPDATE users SET deleted = true ,deleted_at= CURRENT_TIMESTAMP WHERE id=?";
    public static final String BEARER = "Bearer ";
    public static final String INVALID = "INVALID_USERNAME_OR_PASSWORD";
    public static final String INVALID_USERNAME = "USERNAME_NULL_OR_EMPTY";
    public static final String INVALID_PASSWORD = "PASSWORD_NULL_OR_EMPTY";
    public static final String INVALID_FIRST_NAME = "FIRST_NAME_NULL_OR_EMPTY";
    public static final String INVALID_LAST_NAME = "LAST_NAME_NULL_OR_EMPTY";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String USER_NAME_ALREADY_EXIST = "USER_NAME_ALREADY_EXIST";
    public static final String INVALID_REQUEST_BODY = "INVALID_REQUEST_BODY";
    public static final String INVALID_USERNAME_OR_PASSWORD = "INVALID_USERNAME_OR_PASSWORD";
    public static final String PASSWORD_NOT_MATCH = "PASSWORD_DOES_NOT_MATCH";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String AUTHORIZATION = "Authorization";
    public static final String USER_NOT_FOUND = "User Not Found";
    public static final String USER_NOT_EXIST = "User Not Exist";
    public static final String INVALID_ROLE = "ROLE_NULL_OR_EMPTY";
    public static final String DELETE_FALSE = "deleted=false";
}
